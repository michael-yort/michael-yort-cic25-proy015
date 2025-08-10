package es.cic.curso25.proy015.service;

import es.cic.curso25.proy015.exception.RecursoNoEncontradoException;
import es.cic.curso25.proy015.exception.VehiculoNoCompatibleException;
import es.cic.curso25.proy015.model.Estancia;
import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.AsignacionPlazaRepository;
import es.cic.curso25.proy015.repository.EstanciaRepository;
import es.cic.curso25.proy015.repository.PlazaRepository;
import es.cic.curso25.proy015.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EstanciaService {

    private final EstanciaRepository estanciaRepo;
    private final AsignacionPlazaRepository asignacionRepo;
    private final VehiculoRepository vehiculoRepo;
    private final PlazaRepository plazaRepo;
    private final MultaService multaService;

    public EstanciaService(EstanciaRepository estanciaRepo,
            AsignacionPlazaRepository asignacionRepo,
            VehiculoRepository vehiculoRepo,
            PlazaRepository plazaRepo,
            MultaService multaService) {
        this.estanciaRepo = estanciaRepo;
        this.asignacionRepo = asignacionRepo;
        this.vehiculoRepo = vehiculoRepo;
        this.plazaRepo = plazaRepo;
        this.multaService = multaService;
    }

    public List<Estancia> listarEstancias() {
        return estanciaRepo.findAll();
    }

    public Estancia obtenerEstancia(Long id) {
        return estanciaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estancia", id));
    }

    @Transactional
    public Estancia crearEstancia(Estancia estancia) {
        if (estancia.getVehiculo() == null || estancia.getVehiculo().getId() == null) {
            throw new IllegalArgumentException("Vehículo obligatorio");
        }
        if (estancia.getPlaza() == null || estancia.getPlaza().getId() == null) {
            throw new IllegalArgumentException("Plaza obligatoria");
        }

        Long vehiculoId = estancia.getVehiculo().getId();
        Long plazaId = estancia.getPlaza().getId();

        // Reglas de unicidad “abierta”
        if (estanciaRepo.findByVehiculoIdAndSalidaTsIsNull(vehiculoId).isPresent()) {
            throw new IllegalStateException("El vehículo ya tiene una estancia abierta");
        }
        if (estanciaRepo.findByPlazaIdAndSalidaTsIsNull(plazaId).isPresent()) {
            throw new IllegalStateException("La plaza ya está ocupada por otra estancia");
        }

        // Cargar entidades reales
        Vehiculo vehiculo = vehiculoRepo.findById(vehiculoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo", vehiculoId));
        Plaza plaza = plazaRepo.findById(plazaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plaza", plazaId));

        // Validaciones de negocio
        if (!plaza.isActiva()) {
            throw new IllegalStateException("La plaza no está activa");
        }
        if (plaza.getTipo() != vehiculo.getTipo()) {
            throw new VehiculoNoCompatibleException();
        }
        // (Opcional) validar estado del vehículo, etc.

        // Preparar estancia (aún sin guardar)
        estancia.setVehiculo(vehiculo);
        estancia.setPlaza(plaza);
        estancia.setEntradaTs(LocalDateTime.now());
        estancia.setSalidaTs(null);

        // Reglas de multa
        asignacionRepo.findByVehiculoIdAndHastaIsNull(vehiculoId).ifPresentOrElse(asig -> {
            if (!asig.getPlaza().getId().equals(plazaId)) {
                Multa m = new Multa();
                m.setVehiculo(vehiculo);
                m.setEstancia(estancia);
                m.setMotivo("Aparcó en plaza distinta a la asignada");
                multaService.crearMulta(m);
            }
        }, () -> {
            Multa m = new Multa();
            m.setVehiculo(vehiculo);
            m.setEstancia(estancia);
            m.setMotivo("Aparcó sin plaza asignada");
            multaService.crearMulta(m);
        });

        // Guardar al final (dentro de la transacción)
        return estanciaRepo.save(estancia);
    }

    @Transactional
    public Estancia cerrarEstancia(Long id) {
        Estancia e = estanciaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estancia", id));

        if (e.getSalidaTs() != null) {
            throw new IllegalStateException("La estancia ya está cerrada");
        }

        e.setSalidaTs(LocalDateTime.now());
        return estanciaRepo.save(e);
    }

}
