package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.AsignacionPlaza;
import es.cic.curso25.proy015.service.AsignacionPlazaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<AsignacionPlaza> opt = asignacionService.asignacionVigenteVehiculo(vehiculoId);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opt.get());
    }

    @PostMapping
    public ResponseEntity<AsignacionPlaza> crearAsignacion(@RequestBody AsignacionPlaza asignacion) {
        AsignacionPlaza creada = asignacionService.crearAsignacion(asignacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}/cerrar")
    public ResponseEntity<AsignacionPlaza> cerrarAsignacion(@PathVariable Long id) {
        Optional<AsignacionPlaza> opt = asignacionService.cerrarAsignacion(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(opt.get());
    }
}
