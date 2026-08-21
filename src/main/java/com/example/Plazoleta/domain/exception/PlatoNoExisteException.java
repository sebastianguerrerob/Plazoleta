package com.example.Plazoleta.domain.exception;

public class PlatoNoExisteException extends DomainException {

    public PlatoNoExisteException() {
        super("El plato no existe");
    }
}
