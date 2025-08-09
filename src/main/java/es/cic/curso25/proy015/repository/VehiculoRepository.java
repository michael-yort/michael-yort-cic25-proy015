package es.cic.curso25.proy015.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.cic.curso25.proy015.enums.EstadoVehiculo;
import es.cic.curso25.proy015.model.Vehiculo;

import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);

    Optional<Vehiculo> findByMatriculaAndEstado(String matricula, EstadoVehiculo estado);
}
