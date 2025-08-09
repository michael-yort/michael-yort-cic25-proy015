package es.cic.curso25.proy015.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.cic.curso25.proy015.model.Estancia;
import java.util.List;
import java.util.Optional;

public interface EstanciaRepository extends JpaRepository<Estancia, Long> {
    Optional<Estancia> findByVehiculoIdAndSalidaTsIsNull(Long vehiculoId);

    Optional<Estancia> findByPlazaIdAndSalidaTsIsNull(Long plazaId);

    List<Estancia> findByVehiculoId(Long vehiculoId);

    Optional<Estancia> findTopByVehiculoIdOrderByEntradaTsDesc(Long vehiculoId);
}
