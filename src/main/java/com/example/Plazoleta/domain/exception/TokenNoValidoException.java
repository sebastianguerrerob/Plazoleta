package com.example.Plazoleta.domain.exception;

public class TokenNoValidoException extends DomainException {

    public TokenNoValidoException() {
        super("Token de autenticación inválido o expirado");
    }
}
