package es.cic.curso25.proy015.exception;

public class PlazaDuplicadaException extends RuntimeException {
    public PlazaDuplicadaException(String codigo) {
        
        super("Ya existe una plaza con el código: " + codigo);
    }
}
