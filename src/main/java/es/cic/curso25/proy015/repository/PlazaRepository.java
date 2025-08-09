package es.cic.curso25.proy015.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import es.cic.curso25.proy015.model.Plaza;

import java.util.Optional;

public interface PlazaRepository extends JpaRepository<Plaza, Long> {
    Optional<Plaza> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}