package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.Estancia;
import es.cic.curso25.proy015.service.EstanciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estancias")
public class EstanciaController {

    private final EstanciaService estanciaService;

    public EstanciaController(EstanciaService estanciaService) {
        this.estanciaService = estanciaService;
    }

    @GetMapping
    public List<Estancia> listarEstancias() {
        return estanciaService.listarEstancias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estancia> obtenerEstancia(@PathVariable Long id) {
        return estanciaService.obtenerEstancia(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Estancia> crearEstancia(@RequestBody Estancia estancia) {
        Estancia creada = estanciaService.crearEstancia(estancia);
        return ResponseEntity.status(201).body(creada);
    }

    @PutMapping("/{id}/salida")
    public ResponseEntity<Estancia> cerrarEstancia(@PathVariable Long id) {
        return estanciaService.cerrarEstancia(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
