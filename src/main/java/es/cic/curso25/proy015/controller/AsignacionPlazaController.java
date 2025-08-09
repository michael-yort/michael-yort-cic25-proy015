package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.AsignacionPlaza;
import es.cic.curso25.proy015.service.AsignacionPlazaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignaciones")
public class AsignacionPlazaController {

    private final AsignacionPlazaService asignacionService;

    public AsignacionPlazaController(AsignacionPlazaService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @GetMapping
    public List<AsignacionPlaza> listarAsignaciones() {
        return asignacionService.listarAsignaciones();
    }

    @GetMapping("/plaza/{plazaId}")
    public List<AsignacionPlaza> asignacionesVigentesPlaza(@PathVariable Long plazaId) {
        return asignacionService.asignacionesVigentesPlaza(plazaId);
    }

    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<AsignacionPlaza> asignacionVigenteVehiculo(@PathVariable Long vehiculoId) {
        return asignacionService.asignacionVigenteVehiculo(vehiculoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearAsignacion(@RequestBody AsignacionPlaza asignacion) {
        try {
            AsignacionPlaza creada = asignacionService.crearAsignacion(asignacion);
            return ResponseEntity.status(201).body(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<?> cerrarAsignacion(@PathVariable Long id) {
        return asignacionService.cerrarAsignacion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
