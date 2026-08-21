package com.example.Plazoleta.domain.api;

import com.example.Plazoleta.domain.model.Plato;

public interface IPlatoServicePort {
    void crearPlato(Plato plato, Long idPropietario);
}
