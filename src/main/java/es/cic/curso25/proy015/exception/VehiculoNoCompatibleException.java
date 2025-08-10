package es.cic.curso25.proy015.exception;

public class VehiculoNoCompatibleException extends RuntimeException {
    public VehiculoNoCompatibleException() {
        super("El tipo de vehículo no es compatible con la plaza seleccionada.");
    }
}
