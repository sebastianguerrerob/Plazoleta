package com.example.Plazoleta.infrastructure.input.rest.handler;

import com.example.Plazoleta.domain.exception.*;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

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

    @ExceptionHandler(RestauranteNoExisteException.class)
    public ResponseEntity<Map<String, String>> handleRestauranteNoExiste(RestauranteNoExisteException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PlatoNoExisteException.class)
    public ResponseEntity<Map<String, String>> handlePlatoNoExiste(PlatoNoExisteException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PedidoEnProcesoException.class)
    public ResponseEntity<Map<String, String>> handlePedidoEnProceso(PedidoEnProcesoException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
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

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<Map<String, String>> handleMissingHeader(MissingRequestHeaderException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Header Authorization es requerido");
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, String>> handleFeignException(FeignException ex) {
        return buildResponse(HttpStatus.SERVICE_UNAVAILABLE, "Error de comunicación con el servicio de usuarios");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private ResponseEntity<Map<String, String>> buildResponse(HttpStatus status, String mensaje) {
        Map<String, String> body = new HashMap<>();
        body.put("error", mensaje);
        return ResponseEntity.status(status).body(body);
    }
}
