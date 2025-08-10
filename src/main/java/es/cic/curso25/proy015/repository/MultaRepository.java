package es.cic.curso25.proy015.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import es.cic.curso25.proy015.model.Multa;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MultaRepository extends JpaRepository<Multa, Long> {

    //Listar multas pendientes de un vehículo.
    List<Multa> findByVehiculoIdAndLiquidadaFalse(Long vehiculoId);

    //Calcular el importe total pendiente.
    @Query("select coalesce(sum(m.importe), 0) from Multa m where m.vehiculo.id = :vehiculoId and m.liquidada = false")
    BigDecimal sumImportePendienteByVehiculo(Long vehiculoId);

    //Marcar todas las multas pendientes como liquidadas de una sola vez.
    @Modifying
    @Transactional
    @Query("update Multa m set m.liquidada = true where m.vehiculo.id = :vehiculoId and m.liquidada = false")
    int liquidarPendientesPorVehiculo(Long vehiculoId);
}
