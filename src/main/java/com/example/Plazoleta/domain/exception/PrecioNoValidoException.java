package com.example.Plazoleta.domain.exception;

public class PrecioNoValidoException extends DomainException {

    public PrecioNoValidoException() {
        super("El precio debe ser un número entero positivo mayor a 0");
    }
}
