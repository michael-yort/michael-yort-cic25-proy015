package es.cic.curso25.proy015.service;

import es.cic.curso25.proy015.enums.EstadoVehiculo;
import es.cic.curso25.proy015.enums.VehiculoTipo;
import es.cic.curso25.proy015.exception.RecursoNoEncontradoException;
import es.cic.curso25.proy015.exception.VehiculoDuplicadoException;
import es.cic.curso25.proy015.exception.VehiculoNoCompatibleException;
import es.cic.curso25.proy015.exception.PlazaDuplicadaException; // puedes crear una VehiculoDuplicadoException si quieres
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.repository.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepo;

    public VehiculoService(VehiculoRepository vehiculoRepo) {
        this.vehiculoRepo = vehiculoRepo;
    }

    public List<Vehiculo> listarVehiculos() {
        return vehiculoRepo.findAll();
    }

    public Vehiculo obtenerVehiculo(Long id) {
        return vehiculoRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo", id));
    }

    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        if (vehiculo.getTipo() == null ||
                (vehiculo.getTipo() != VehiculoTipo.COCHE &&
                        vehiculo.getTipo() != VehiculoTipo.CARAVANA &&
                        vehiculo.getTipo() != VehiculoTipo.MOTO)) {
            throw new VehiculoNoCompatibleException();
        }

        if (vehiculoRepo.existsByMatricula(vehiculo.getMatricula())) {
            throw new VehiculoDuplicadoException(vehiculo.getMatricula());
        }

        vehiculo.setEstado(EstadoVehiculo.ALTA);
        return vehiculoRepo.save(vehiculo);
    }

    public void darDeBajaVehiculo(Long id) {
        Vehiculo vehiculo = obtenerVehiculo(id); // lanza RecursoNoEncontradoException si no existe
        vehiculo.setEstado(EstadoVehiculo.BAJA);
        vehiculoRepo.save(vehiculo);
    }
}
