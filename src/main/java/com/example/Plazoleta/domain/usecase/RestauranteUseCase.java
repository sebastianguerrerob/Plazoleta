package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.model.Propietario;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import com.example.Plazoleta.domain.spi.IUsuarioServicePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestauranteUseCase implements IRestauranteServicePort {

    private IRestaurantePersistencePort restaurantePersistencePort;

    private final IUsuarioServicePort usuarioServicePort;


    @Override
    public void crearRestaurante(Restaurante restaurante) {
        //verificar Usuario propietario
        Propietario propietario = usuarioServicePort.obtenerUsuarioPorId(restaurante.getId_propietario());
        if (!propietario.getRolId().equals(2L)) {
            throw new RuntimeException(
                    "El usuario no es un propietario"
            );
        }
        //verificar numerico NIT


        //verificar Numero

        //verificar Nombre restaurante

    }
}
