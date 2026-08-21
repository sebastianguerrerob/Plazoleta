package com.example.Plazoleta.domain.exception;

public class NombreNoValidoException extends DomainException {

    public NombreNoValidoException() {
        super("El nombre del restaurante no puede contener únicamente números");
    }
}
