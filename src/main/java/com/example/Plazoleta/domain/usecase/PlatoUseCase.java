package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IPlatoServicePort;
import com.example.Plazoleta.domain.exception.PlatoNoExisteException;
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
        if (plato.getPrecio() == null || plato.getPrecio() <= 0) {
            throw new PrecioNoValidoException();
        }

        Restaurante restaurante = restaurantePersistencePort.obtenerRestaurantePorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        if (restaurante.getId_propietario() != idPropietario) {
            throw new PropietarioNoEsDuenoException();
        }

        plato.setActivo(true);
        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public void actualizarPlato(Long idPlato, Integer precio, String descripcion, Long idPropietario) {
        // Obtener el plato
        Plato plato = platoPersistencePort.obtenerPlatoPorId(idPlato)
                .orElseThrow(PlatoNoExisteException::new);

        // Validar que el propietario sea dueño del restaurante al que pertenece el plato
        Restaurante restaurante = restaurantePersistencePort.obtenerRestaurantePorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        if (restaurante.getId_propietario() != idPropietario) {
            throw new PropietarioNoEsDuenoException();
        }

        // Validar precio
        if (precio == null || precio <= 0) {
            throw new PrecioNoValidoException();
        }

        // Solo actualizar precio y descripción
        plato.setPrecio(precio);
        plato.setDescripcion(descripcion);

        platoPersistencePort.guardarPlato(plato);
    }
}
