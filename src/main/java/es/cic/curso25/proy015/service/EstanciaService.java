package es.cic.curso25.proy015.service;

import es.cic.curso25.proy015.model.Estancia;
import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.repository.EstanciaRepository;
import es.cic.curso25.proy015.repository.AsignacionPlazaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EstanciaService {

    private final EstanciaRepository estanciaRepo;
    private final AsignacionPlazaRepository asignacionRepo;
    private final MultaService multaService; // servicio que ya tienes

    public EstanciaService(
            EstanciaRepository estanciaRepo,
            AsignacionPlazaRepository asignacionRepo,
            MultaService multaService) {
        this.estanciaRepo = estanciaRepo;
        this.asignacionRepo = asignacionRepo;
        this.multaService = multaService;
    }

    public List<Estancia> listarEstancias() {
        return estanciaRepo.findAll();
    }

    public Optional<Estancia> obtenerEstancia(Long id) {
        return estanciaRepo.findById(id);
    }

    public Estancia crearEstancia(Estancia estancia) {
        estancia.setEntradaTs(LocalDateTime.now());

        // Guardar primero la estancia
        Estancia guardada = estanciaRepo.save(estancia);

        // Comprobar si el vehículo tiene asignación vigente
        var asignacionOpt = asignacionRepo.findByVehiculoIdAndHastaIsNull(
                estancia.getVehiculo().getId());

        if (asignacionOpt.isPresent()) {
            Long plazaAsignadaId = asignacionOpt.get().getPlaza().getId();
            Long plazaEntradaId = estancia.getPlaza().getId();

            if (!plazaAsignadaId.equals(plazaEntradaId)) {
                Multa m = new Multa();
                m.setVehiculo(guardada.getVehiculo());
                m.setEstancia(guardada);
                m.setMotivo("Aparcó en plaza distinta a la asignada");
                multaService.crearMulta(m); // el importe se pondrá por defecto
            }
        } else {
            // Si quieres multar cuando no tiene asignación, descomenta esto:

            Multa m = new Multa();
            m.setVehiculo(guardada.getVehiculo());
            m.setEstancia(guardada);
            m.setMotivo("Aparcó sin plaza asignada");
            multaService.crearMulta(m);

        }

        return guardada;
    }

    public Optional<Estancia> cerrarEstancia(Long id) {
        return estanciaRepo.findById(id).map(e -> {
            e.setSalidaTs(LocalDateTime.now());
            return estanciaRepo.save(e);
        });
    }
}
