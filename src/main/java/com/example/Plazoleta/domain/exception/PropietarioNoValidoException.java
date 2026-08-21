package com.example.Plazoleta.domain.exception;

public class PropietarioNoValidoException extends DomainException {

    public PropietarioNoValidoException() {
        super("El usuario no tiene el rol de propietario");
    }
}
