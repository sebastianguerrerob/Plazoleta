package com.example.Plazoleta.domain.api;

import com.example.Plazoleta.domain.model.Plato;

public interface IPlatoServicePort {
    void crearPlato(Plato plato, Long idPropietario);
    void actualizarPlato(Long idPlato, Integer precio, String descripcion, Long idPropietario);
}
