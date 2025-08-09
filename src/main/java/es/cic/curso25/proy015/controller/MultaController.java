package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.service.MultaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/multas")
public class MultaController {

    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    @GetMapping
    public List<Multa> listarMultas() {
        return multaService.listarMultas();
    }

    @GetMapping("/vehiculo/{vehiculoId}/pendientes")
    public List<Multa> multasPendientesPorVehiculo(@PathVariable Long vehiculoId) {
        return multaService.multasPendientesPorVehiculo(vehiculoId);
    }

    @PostMapping
    public ResponseEntity<?> crearMulta(@RequestBody Multa multa) {
        try {
            Multa creada = multaService.crearMulta(multa);
            return ResponseEntity.status(201).body(creada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/vehiculo/{vehiculoId}/liquidar")
    public ResponseEntity<?> liquidarMultasVehiculo(@PathVariable Long vehiculoId) {
        try {
            MultaService.ResultadoLiquidacion r = multaService.liquidarMultasVehiculo(vehiculoId);
            return ResponseEntity.ok(r); // { "numeroMultas": X, "total": Y }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
