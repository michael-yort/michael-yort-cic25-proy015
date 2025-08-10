package es.cic.curso25.proy015.controller;

import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.service.MultaService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Multa> crearMulta(@RequestBody Multa multa) {
        Multa creada = multaService.crearMulta(multa);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/vehiculo/{vehiculoId}/liquidar")
    public MultaService.ResultadoLiquidacion liquidarMultasVehiculo(@PathVariable Long vehiculoId) {
        return multaService.liquidarMultasVehiculo(vehiculoId);
    }
}
