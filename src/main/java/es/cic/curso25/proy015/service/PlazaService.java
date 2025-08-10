package es.cic.curso25.proy015.service;

import es.cic.curso25.proy015.exception.PlazaDuplicadaException;
import es.cic.curso25.proy015.exception.RecursoNoEncontradoException;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.repository.PlazaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlazaService {

    private final PlazaRepository plazaRepo;

    public PlazaService(PlazaRepository plazaRepo) {
        this.plazaRepo = plazaRepo;
    }

    public List<Plaza> listarPlazas() {
        return plazaRepo.findAll();
    }

    public Plaza obtenerPlaza(Long id) {
        return plazaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Plaza", id));
    }

    public Plaza crearPlaza(Plaza plaza) {
        if (plaza.getCodigo() == null || plaza.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código de plaza es obligatorio");
        }
        if (plaza.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de plaza es obligatorio");
        }
        if (plazaRepo.existsByCodigo(plaza.getCodigo())) {
            throw new PlazaDuplicadaException(plaza.getCodigo());
        }
        return plazaRepo.save(plaza);
    }

    public Plaza actualizarPlaza(Long id, Plaza datos) {
        Plaza existente = obtenerPlaza(id); // lanza RecursoNoEncontradoException si no existe

        if (datos.getCodigo() != null && !datos.getCodigo().isBlank()
                && !datos.getCodigo().equals(existente.getCodigo())) {
            if (plazaRepo.existsByCodigo(datos.getCodigo())) {
                throw new PlazaDuplicadaException(datos.getCodigo());
            }
            existente.setCodigo(datos.getCodigo());
        }

        if (datos.getTipo() != null) {
            existente.setTipo(datos.getTipo());
        }

        existente.setActiva(datos.isActiva());

        return plazaRepo.save(existente);
    }
}
