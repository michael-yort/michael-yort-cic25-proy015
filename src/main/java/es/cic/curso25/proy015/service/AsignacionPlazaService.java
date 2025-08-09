package es.cic.curso25.proy015.service;

import es.cic.curso25.proy015.model.AsignacionPlaza;
import es.cic.curso25.proy015.repository.AsignacionPlazaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AsignacionPlazaService {

    private final AsignacionPlazaRepository asigRepo;

    public AsignacionPlazaService(AsignacionPlazaRepository asigRepo) {
        this.asigRepo = asigRepo;
    }

    public List<AsignacionPlaza> listarAsignaciones() {
        return asigRepo.findAll();
    }

    public List<AsignacionPlaza> asignacionesVigentesPlaza(Long plazaId) {
        return asigRepo.findAllByPlazaIdAndHastaIsNull(plazaId);
    }

    public Optional<AsignacionPlaza> asignacionVigenteVehiculo(Long vehiculoId) {
        return asigRepo.findByVehiculoIdAndHastaIsNull(vehiculoId);
    }

    /**
     * Crea asignación vigente validando reglas: max 5 por plaza y 1 por vehículo
     */
    public AsignacionPlaza crearAsignacion(AsignacionPlaza asignacion) {
        if (asignacion.getPlaza() == null || asignacion.getPlaza().getId() == null) {
            throw new IllegalArgumentException("Debe indicar la plaza");
        }
        if (asignacion.getVehiculo() == null || asignacion.getVehiculo().getId() == null) {
            throw new IllegalArgumentException("Debe indicar el vehículo");
        }

        long vigentesEnPlaza = asigRepo.countByPlazaIdAndHastaIsNull(asignacion.getPlaza().getId());
        if (vigentesEnPlaza >= 2) {
            throw new IllegalStateException("La plaza ya tiene 5 vehículos asignados");
        }

        if (asigRepo.findByVehiculoIdAndHastaIsNull(asignacion.getVehiculo().getId()).isPresent()) {
            throw new IllegalStateException("El vehículo ya tiene una plaza asignada vigente");
        }

        // por si no viene la fecha
        if (asignacion.getDesde() == null) {
            asignacion.setDesde(LocalDateTime.now());
        }
        asignacion.setHasta(null);

        return asigRepo.save(asignacion);
    }

    /** Cierra una asignación (deja de ser vigente) */
    public Optional<AsignacionPlaza> cerrarAsignacion(Long id) {
        return asigRepo.findById(id).map(a -> {
            if (a.getHasta() == null) {
                a.setHasta(LocalDateTime.now());
                return asigRepo.save(a);
            }
            return a; // ya estaba cerrada
        });
    }
}
