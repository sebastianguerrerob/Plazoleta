package com.example.Plazoleta.domain.exception;

public class TelefonoNoValidoException extends DomainException {

    public TelefonoNoValidoException() {
        super("El teléfono debe ser numérico, puede contener el símbolo + y tener máximo 13 caracteres");
    }
}
