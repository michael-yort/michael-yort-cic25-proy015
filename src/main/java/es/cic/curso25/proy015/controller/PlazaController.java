package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.service.PlazaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Plaza obtenerPlaza(@PathVariable Long id) {
        return plazaService.obtenerPlaza(id);
    }

    @PostMapping
    public ResponseEntity<Plaza> crearPlaza(@RequestBody Plaza plaza) {
        Plaza creada = plazaService.crearPlaza(plaza);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public Plaza actualizarPlaza(@PathVariable Long id, @RequestBody Plaza datos) {
        return plazaService.actualizarPlaza(id, datos);
    }
}
