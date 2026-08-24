package com.example.Plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedResult<T> {
    private final List<T> contenido;
    private final int pagina;
    private final int tamano;
    private final long totalElementos;
    private final int totalPaginas;
}
