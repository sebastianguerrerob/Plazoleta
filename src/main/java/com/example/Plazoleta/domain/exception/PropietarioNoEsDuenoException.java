package com.example.Plazoleta.domain.exception;

public class PropietarioNoEsDuenoException extends DomainException {

    public PropietarioNoEsDuenoException() {
        super("El usuario no es propietario de este restaurante");
    }
}
