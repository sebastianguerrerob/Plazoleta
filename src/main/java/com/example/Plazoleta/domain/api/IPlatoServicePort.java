package com.example.Plazoleta.domain.api;

import com.example.Plazoleta.domain.model.Plato;

public interface IPlatoServicePort {
    void crearPlato(Plato plato, String token);
    void actualizarPlato(Long idPlato, Integer precio, String descripcion, String token);
}
