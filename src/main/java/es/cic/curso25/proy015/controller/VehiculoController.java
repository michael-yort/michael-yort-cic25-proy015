package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.service.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<Vehiculo> listarVehiculos() {
        return vehiculoService.listarVehiculos();
    }

    @GetMapping("/{id}")
    public Vehiculo obtenerVehiculo(@PathVariable Long id) {
        return vehiculoService.obtenerVehiculo(id);
    }

    @PostMapping
    public ResponseEntity<Vehiculo> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        Vehiculo creado = vehiculoService.crearVehiculo(vehiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> bajaVehiculo(@PathVariable Long id) {
        vehiculoService.darDeBajaVehiculo(id);
        return ResponseEntity.noContent().build();
    }
}
