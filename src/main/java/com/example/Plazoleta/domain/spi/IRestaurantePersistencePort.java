package com.example.Plazoleta.domain.spi;

import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Restaurante;

import java.util.Optional;

public interface IRestaurantePersistencePort {
    void guardarRestaurante(Restaurante restaurante);
    Optional<Restaurante> obtenerRestaurantePorId(Long id);
    PaginatedResult<Restaurante> listarRestaurantesOrdenadosPorNombre(PaginationRequest paginationRequest);
}
