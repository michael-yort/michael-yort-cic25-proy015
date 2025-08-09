package es.cic.curso25.proy015.service;

import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.model.Estancia;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.MultaRepository;
import es.cic.curso25.proy015.repository.EstanciaRepository;
import es.cic.curso25.proy015.repository.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MultaService {

    public static class ResultadoLiquidacion {
        private int numeroMultas;
        private BigDecimal total;

        public ResultadoLiquidacion(int numeroMultas, BigDecimal total) {
            this.numeroMultas = numeroMultas;
            this.total = total;
        }

        public int getNumeroMultas() {
            return numeroMultas;
        }

        public BigDecimal getTotal() {
            return total;
        }
    }

    private final MultaRepository multaRepo;
    private final VehiculoRepository vehiculoRepo;
    private final EstanciaRepository estanciaRepo;

    public MultaService(MultaRepository multaRepo,
            VehiculoRepository vehiculoRepo,
            EstanciaRepository estanciaRepo) {
        this.multaRepo = multaRepo;
        this.vehiculoRepo = vehiculoRepo;
        this.estanciaRepo = estanciaRepo;
    }

    public List<Multa> listarMultas() {
        return multaRepo.findAll();
    }

    public List<Multa> multasPendientesPorVehiculo(Long vehiculoId) {
        return multaRepo.findByVehiculoIdAndLiquidadaFalse(vehiculoId);
    }

    public Multa crearMulta(Multa multa) {
        if (multa.getVehiculo() == null || multa.getVehiculo().getId() == null) {
            throw new IllegalArgumentException("Debe indicar el vehículo");
        }

        Vehiculo vehiculo = vehiculoRepo.findById(multa.getVehiculo().getId())
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

        Estancia estancia = null;
        if (multa.getEstancia() != null && multa.getEstancia().getId() != null) {
            estancia = estanciaRepo.findById(multa.getEstancia().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Estancia no encontrada"));
        }

        if (multa.getMotivo() == null || multa.getMotivo().isBlank()) {
            multa.setMotivo("Aparcó en plaza distinta a la asignada");
        }

        if (multa.getImporte() == null) {
            multa.setImporte(new BigDecimal("5.00"));
        }
        multa.setLiquidada(false);

        multa.setVehiculo(vehiculo);
        multa.setEstancia(estancia);

        return multaRepo.save(multa);
    }

    /** Devuelve número de multas liquidadas e importe total liquidado */
    public ResultadoLiquidacion liquidarMultasVehiculo(Long vehiculoId) {
        vehiculoRepo.findById(vehiculoId)
                .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado"));

        BigDecimal total = multaRepo.sumImportePendienteByVehiculo(vehiculoId);
        int count = multaRepo.liquidarPendientesPorVehiculo(vehiculoId);

        return new ResultadoLiquidacion(count, total);
    }
}
