package com.example.Plazoleta.domain.exception;

public class RolNoAutorizadoException extends DomainException {

    public RolNoAutorizadoException() {
        super("No tiene permisos para realizar esta acción");
    }
}
