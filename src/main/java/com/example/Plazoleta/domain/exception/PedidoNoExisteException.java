package com.example.Plazoleta.domain.exception;

public class PedidoNoExisteException extends DomainException {

    public PedidoNoExisteException() {
        super("El pedido no existe");
    }
}
