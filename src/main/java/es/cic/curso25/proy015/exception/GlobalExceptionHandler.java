package es.cic.curso25.proy015.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 Not Found
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<String> handleNotFound(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 409 Conflict (duplicados y estados de negocio en conflicto)
    @ExceptionHandler({ PlazaDuplicadaException.class, VehiculoDuplicadoException.class, IllegalStateException.class })
    public ResponseEntity<String> handleConflict(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // 400 Bad Request (datos inválidos / incompatibilidades)
    @ExceptionHandler({ VehiculoNoCompatibleException.class, IllegalArgumentException.class })
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // (Opcional) 500 - por si se escapa algo no contemplado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
    }
}
