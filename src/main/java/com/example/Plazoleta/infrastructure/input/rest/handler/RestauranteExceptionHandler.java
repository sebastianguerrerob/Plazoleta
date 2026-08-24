package com.example.Plazoleta.infrastructure.input.rest.handler;

import com.example.Plazoleta.domain.exception.DomainException;
import com.example.Plazoleta.domain.exception.PropietarioNoEsDuenoException;
import com.example.Plazoleta.domain.exception.PropietarioNoValidoException;
import com.example.Plazoleta.domain.exception.RolNoAutorizadoException;
import com.example.Plazoleta.domain.exception.TokenNoValidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestauranteExceptionHandler {

    @ExceptionHandler(TokenNoValidoException.class)
    public ResponseEntity<Map<String, String>> handleTokenNoValido(TokenNoValidoException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(RolNoAutorizadoException.class)
    public ResponseEntity<Map<String, String>> handleRolNoAutorizado(RolNoAutorizadoException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(PropietarioNoValidoException.class)
    public ResponseEntity<Map<String, String>> handlePropietarioNoValido(PropietarioNoValidoException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(PropietarioNoEsDuenoException.class)
    public ResponseEntity<Map<String, String>> handlePropietarioNoEsDueno(PropietarioNoEsDuenoException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, String>> handleDomainException(DomainException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Error de validación");
        return buildResponse(HttpStatus.BAD_REQUEST, mensaje);
    }

    private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String mensaje) {
        Map<String, String> body = new HashMap<>();
        body.put("error", mensaje);
        return ResponseEntity.status(status).body(body);
    }
}
