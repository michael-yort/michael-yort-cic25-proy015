package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.Estancia;
import es.cic.curso25.proy015.service.EstanciaService;
import org.springframework.http.HttpStatus;
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
    public Estancia obtenerEstancia(@PathVariable Long id) {
        return estanciaService.obtenerEstancia(id); // lanza 404 si no existe
    }

    @PostMapping
    public ResponseEntity<Estancia> crearEstancia(@RequestBody Estancia estancia) {
        Estancia creada = estanciaService.crearEstancia(estancia); // valida y lanza 400/409 si procede
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}/salida")
    public Estancia cerrarEstancia(@PathVariable Long id) {
        return estanciaService.cerrarEstancia(id); // 404 si no existe, 409 si ya estaba cerrada
    }
}
