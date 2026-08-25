package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.exception.*;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestauranteUseCase implements IRestauranteServicePort {

    private final IRestaurantePersistencePort restaurantePersistencePort;
    private static final String REGEX_NUM = "\\d+";
    private static final String REGEX_CELULAR = "^\\+?\\d{1,12}$";

    @Override
    public void crearRestaurante(Restaurante restaurante) {
        // Verificar que el NIT sea numérico
        if (!restaurante.getNit().matches(REGEX_NUM)) {
            throw new NitNoNumericoException();
        }

        // Verificar formato de teléfono
        if (!restaurante.getTelefono().matches(REGEX_CELULAR)) {
            throw new TelefonoNoValidoException();
        }

        // Verificar que el nombre no sea solo números
        if (restaurante.getNombre().matches(REGEX_NUM)) {
            throw new NombreNoValidoException();
        }

        restaurantePersistencePort.guardarRestaurante(restaurante);
    }

    @Override
    public PaginatedResult<Restaurante> listarRestaurantes(PaginationRequest paginationRequest) {
        return restaurantePersistencePort.listarRestaurantesOrdenadosPorNombre(paginationRequest);
    }

    @Override
    public boolean validarPropietarioRestaurante(Long idRestaurante, Long idPropietario) {
        return restaurantePersistencePort.obtenerRestaurantePorId(idRestaurante)
                .map(restaurante -> restaurante.getIdPropietario().equals(idPropietario))
                .orElse(false);
    }
}
