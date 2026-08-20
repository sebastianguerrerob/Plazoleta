package com.example.Plazoleta.domain.spi;

import com.example.Plazoleta.domain.model.Restaurante;

public interface IPlazoletaPersistencePort {
    void guardarRestaurante(Restaurante restaurante);
}
