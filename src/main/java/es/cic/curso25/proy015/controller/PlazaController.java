package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.service.PlazaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/plazas")
public class PlazaController {

    private final PlazaService plazaService;

    public PlazaController(PlazaService plazaService) {
        this.plazaService = plazaService;
    }

    @GetMapping
    public List<Plaza> listarPlazas() {
        return plazaService.listarPlazas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plaza> obtenerPlaza(@PathVariable Long id) {
        return plazaService.obtenerPlaza(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearPlaza(@RequestBody Plaza plaza) {
        try {
            Plaza creada = plazaService.crearPlaza(plaza);
            return ResponseEntity.status(201).body(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPlaza(@PathVariable Long id, @RequestBody Plaza datos) {
        Optional<Plaza> plazaActualizada = plazaService.actualizarPlaza(id, datos);

        if (plazaActualizada.isPresent()) {
            return ResponseEntity.ok(plazaActualizada.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
