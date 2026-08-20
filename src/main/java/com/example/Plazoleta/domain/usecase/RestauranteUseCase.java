package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestauranteUseCase implements IRestauranteServicePort {

    private IRestaurantePersistencePort restaurantePersistencePort;



    @Override
    public void crearRestaurante(Restaurante restaurante) {
        //verificar Usuario propietario

        //verificar numerico NIT

        //verificar Numero

        //verificar Nombre restaurante

    }
}
