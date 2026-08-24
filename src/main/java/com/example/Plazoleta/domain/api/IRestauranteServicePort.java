package com.example.Plazoleta.domain.api;

import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Restaurante;

public interface IRestauranteServicePort {
    void crearRestaurante(Restaurante restaurante);
    PaginatedResult<Restaurante> listarRestaurantes(PaginationRequest paginationRequest);
}
