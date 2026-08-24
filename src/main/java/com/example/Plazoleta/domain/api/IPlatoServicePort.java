package com.example.Plazoleta.domain.api;

import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Plato;

public interface IPlatoServicePort {
    void crearPlato(Plato plato, Long idPropietario);
    void actualizarPlato(Long idPlato, Integer precio, String descripcion, Long idPropietario);
    void cambiarEstadoPlato(Long idPlato, Boolean activo, Long idPropietario);
    PaginatedResult<Plato> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, PaginationRequest paginationRequest);
}
