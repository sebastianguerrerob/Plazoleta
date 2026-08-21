package com.example.Plazoleta.domain.exception;

public class NitNoNumericoException extends DomainException {

    public NitNoNumericoException() {
        super("El NIT debe ser únicamente numérico");
    }
}
