package com.example.Plazoleta.domain.spi;

import com.example.Plazoleta.domain.model.Plato;

public interface IPlatoPersistencePort {
    void guardarPlato(Plato plato);
}
