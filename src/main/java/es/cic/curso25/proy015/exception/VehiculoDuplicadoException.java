package es.cic.curso25.proy015.exception;

public class VehiculoDuplicadoException extends RuntimeException {
    public VehiculoDuplicadoException(String matricula) {
        super("Ya existe un vehículo con la matrícula: " + matricula);
    }
}
