package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IPlatoServicePort;
import com.example.Plazoleta.domain.exception.PrecioNoValidoException;
import com.example.Plazoleta.domain.exception.PropietarioNoEsDuenoException;
import com.example.Plazoleta.domain.exception.RestauranteNoExisteException;
import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatoUseCase implements IPlatoServicePort {

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;

    @Override
    public void crearPlato(Plato plato, Long idPropietario) {
        // Validar que el precio sea mayor a 0
        if (plato.getPrecio() == null || plato.getPrecio() <= 0) {
            throw new PrecioNoValidoException();
        }

        // Validar que el restaurante exista
        Restaurante restaurante = restaurantePersistencePort.obtenerRestaurantePorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        // Validar que el propietario sea dueño del restaurante
        if (restaurante.getId_propietario() != idPropietario) {
            throw new PropietarioNoEsDuenoException();
        }

        // Por defecto el plato se crea activo
        plato.setActivo(true);

        platoPersistencePort.guardarPlato(plato);
    }
}
