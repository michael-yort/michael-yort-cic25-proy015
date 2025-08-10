package es.cic.curso25.proy015.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String recurso, Long id) {
        super(recurso + " con ID " + id + " no encontrado.");
    }
}
