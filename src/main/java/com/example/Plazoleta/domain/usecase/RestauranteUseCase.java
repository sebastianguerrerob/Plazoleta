package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.exception.*;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IAuthServicePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestauranteUseCase implements IRestauranteServicePort {

    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IAuthServicePort authServicePort;
    private static final String REGEX_NUM = "\\d+";
    private static final String REGEX_CELULAR = "^\\+?\\d{1,12}$";
    private static final String ROL_ADMIN = "ADMIN";

    @Override
    public void crearRestaurante(Restaurante restaurante, String token) {
        // Validar token y obtener usuario autenticado
        AuthUser authUser = authServicePort.validateToken(token);
        if (authUser == null || !Boolean.TRUE.equals(authUser.getValid())) {
            throw new TokenNoValidoException();
        }

        // Verificar que el rol sea ADMIN
        if (!ROL_ADMIN.equalsIgnoreCase(authUser.getRol())) {
            throw new RolNoAutorizadoException();
        }

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
}
