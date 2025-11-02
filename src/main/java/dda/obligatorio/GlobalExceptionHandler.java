package dda.obligatorio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import dda.obligatorio.modelo.PeajeException;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(PeajeException.class)
    public ResponseEntity<String> manejarException(PeajeException ex) {
        return ResponseEntity.status(299).body(ex.getMessage());
    }
}
