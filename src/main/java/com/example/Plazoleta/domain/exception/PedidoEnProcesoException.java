package com.example.Plazoleta.domain.exception;

public class PedidoEnProcesoException extends DomainException {

    public PedidoEnProcesoException() {
        super("El cliente ya tiene un pedido en proceso");
    }
}
