package es.cic.curso25.proy015.service;

import es.cic.curso25.proy015.enums.EstadoVehiculo;
import es.cic.curso25.proy015.enums.VehiculoTipo;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepo;

    public VehiculoService(VehiculoRepository vehiculoRepo) {
        this.vehiculoRepo = vehiculoRepo;
    }

    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepo.findAll();
    }

    public Optional<Vehiculo> obtenerVehiculo(Long id) {
        return vehiculoRepo.findById(id);
    }

    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getTipo() == null ||
                (vehiculo.getTipo() != VehiculoTipo.COCHE &&
                        vehiculo.getTipo() != VehiculoTipo.CARAVANA &&
                        vehiculo.getTipo() != VehiculoTipo.MOTO)) {
            throw new IllegalArgumentException("Tipo de vehículo no permitido");
        }

        if (vehiculoRepo.existsByMatricula(vehiculo.getMatricula())) {
            throw new IllegalStateException("La matrícula ya está registrada");
        }

        vehiculo.setEstado(EstadoVehiculo.ALTA);
        return vehiculoRepo.save(vehiculo);
    }

    public boolean darDeBajaVehiculo(Long id) {
        return vehiculoRepo.findById(id)
                .map(v -> {
                    v.setEstado(EstadoVehiculo.BAJA);
                    vehiculoRepo.save(v);
                    return true;
                })
                .orElse(false);
    }
}
