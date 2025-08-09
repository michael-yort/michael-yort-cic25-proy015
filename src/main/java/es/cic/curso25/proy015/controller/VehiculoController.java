package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.service.VehiculoService;
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
    public ResponseEntity<Vehiculo> obtenerVehiculo(@PathVariable Long id) {
        return vehiculoService.obtenerVehiculo(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearVehiculo(@RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo creado = vehiculoService.crearVehiculo(vehiculo);
            return ResponseEntity.status(201).body(creado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> bajaVehiculo(@PathVariable Long id) {
        boolean dadoDeBaja = vehiculoService.darDeBajaVehiculo(id);
        return dadoDeBaja ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
