package com.example.Plazoleta.domain.spi;

public interface IMensajeriaServicePort {
    void enviarSms(String telefono, String mensaje);
}
