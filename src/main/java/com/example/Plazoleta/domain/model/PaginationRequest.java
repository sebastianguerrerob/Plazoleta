package com.example.Plazoleta.domain.model;

import com.example.Plazoleta.domain.exception.DomainException;
import lombok.Getter;

@Getter
public class PaginationRequest {

    private static final int MAX_SIZE = 50;

    private final int pagina;
    private final int tamano;

    public PaginationRequest(int pagina, int tamano) {
        if (pagina < 0) {
            throw new DomainException("La página no puede ser negativa");
        }
        if (tamano <= 0) {
            throw new DomainException("El tamaño debe ser mayor a 0");
        }
        if (tamano > MAX_SIZE) {
            throw new DomainException("El tamaño máximo permitido es " + MAX_SIZE);
        }
        this.pagina = pagina;
        this.tamano = tamano;
    }
}
