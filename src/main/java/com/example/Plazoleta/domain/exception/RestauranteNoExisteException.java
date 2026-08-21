package com.example.Plazoleta.domain.exception;

public class RestauranteNoExisteException extends DomainException {

    public RestauranteNoExisteException() {
        super("El restaurante no existe");
    }
}
