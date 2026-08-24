package com.example.Plazoleta.domain.spi;

import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Plato;

import java.util.Optional;

public interface IPlatoPersistencePort {
    void guardarPlato(Plato plato);
    Optional<Plato> obtenerPlatoPorId(Long id);
    PaginatedResult<Plato> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, PaginationRequest paginationRequest);
}
