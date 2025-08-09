package es.cic.curso25.proy015.service;

import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.repository.PlazaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlazaService {

    private final PlazaRepository plazaRepo;

    public PlazaService(PlazaRepository plazaRepo) {
        this.plazaRepo = plazaRepo;
    }

    public List<Plaza> listarPlazas() {
        return plazaRepo.findAll();
    }

    public Optional<Plaza> obtenerPlaza(Long id) {
        return plazaRepo.findById(id);
    }

    public Plaza crearPlaza(Plaza plaza) {
        if (plaza.getCodigo() == null || plaza.getCodigo().isBlank()) {
            throw new IllegalArgumentException("El código de plaza es obligatorio");
        }
        if (plaza.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de plaza es obligatorio");
        }
        // Requiere method existsByCodigo en el repo (lo añado abajo)
        if (plazaRepo.existsByCodigo(plaza.getCodigo())) {
            throw new IllegalStateException("El código de plaza ya existe");
        }
        return plazaRepo.save(plaza);
    }

    public Optional<Plaza> actualizarPlaza(Long id, Plaza datos) {
        return plazaRepo.findById(id).map(p -> {
            if (datos.getCodigo() != null && !datos.getCodigo().isBlank()
                    && !datos.getCodigo().equals(p.getCodigo())) {
                if (plazaRepo.existsByCodigo(datos.getCodigo())) {
                    throw new IllegalStateException("El código de plaza ya existe");
                }
                p.setCodigo(datos.getCodigo());
            }
            if (datos.getTipo() != null) {
                p.setTipo(datos.getTipo());
            }
            p.setActiva(datos.isActiva());
            return plazaRepo.save(p);
        });
    }
}
