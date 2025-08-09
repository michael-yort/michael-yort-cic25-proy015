package es.cic.curso25.proy015.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.cic.curso25.proy015.model.AsignacionPlaza;
import java.util.List;
import java.util.Optional;

public interface AsignacionPlazaRepository extends JpaRepository<AsignacionPlaza, Long> {
    Optional<AsignacionPlaza> findByVehiculoIdAndHastaIsNull(Long vehiculoId);
    // SELECT * FROM asignacion_plaza
    // WHERE vehiculo_id = ? AND hasta IS NULL;

    long countByPlazaIdAndHastaIsNull(Long plazaId);
    // SELECT COUNT(*) FROM asignacion_plaza
    // WHERE plaza_id = ? AND hasta IS NULL;

    List<AsignacionPlaza> findAllByPlazaIdAndHastaIsNull(Long plazaId);
    // SELECT * FROM asignacion_plaza
    // WHERE plaza_id = ? AND hasta IS NULL;

}
