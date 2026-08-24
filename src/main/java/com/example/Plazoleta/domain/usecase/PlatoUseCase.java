package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IPlatoServicePort;
import com.example.Plazoleta.domain.exception.*;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IAuthServicePort;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatoUseCase implements IPlatoServicePort {

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IAuthServicePort authServicePort;
    private static final String ROL_PROPIETARIO = "PROPIETARIO";

    @Override
    public void crearPlato(Plato plato, String token) {
        // Validar token y rol PROPIETARIO
        AuthUser authUser = validatePropietario(token);

        if (plato.getPrecio() == null || plato.getPrecio() <= 0) {
            throw new PrecioNoValidoException();
        }

        Restaurante restaurante = restaurantePersistencePort.obtenerRestaurantePorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        // Verificar que el userId del token sea el propietario del restaurante
        if (restaurante.getId_propietario() != authUser.getUserId()) {
            throw new PropietarioNoEsDuenoException();
        }

        plato.setActivo(true);
        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public void actualizarPlato(Long idPlato, Integer precio, String descripcion, String token) {
        // Validar token y rol PROPIETARIO
        AuthUser authUser = validatePropietario(token);

        Plato plato = platoPersistencePort.obtenerPlatoPorId(idPlato)
                .orElseThrow(PlatoNoExisteException::new);

        Restaurante restaurante = restaurantePersistencePort.obtenerRestaurantePorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        // Verificar que el userId del token sea el propietario del restaurante
        if (restaurante.getId_propietario() != authUser.getUserId()) {
            throw new PropietarioNoEsDuenoException();
        }

        if (precio == null || precio <= 0) {
            throw new PrecioNoValidoException();
        }

        plato.setPrecio(precio);
        plato.setDescripcion(descripcion);

        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public void cambiarEstadoPlato(Long idPlato, Boolean activo, String token) {
        AuthUser authUser = validatePropietario(token);

        Plato plato = platoPersistencePort.obtenerPlatoPorId(idPlato)
                .orElseThrow(PlatoNoExisteException::new);

        Restaurante restaurante = restaurantePersistencePort.obtenerRestaurantePorId(plato.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        if (restaurante.getId_propietario() != authUser.getUserId()) {
            throw new PropietarioNoEsDuenoException();
        }

        plato.setActivo(activo);
        platoPersistencePort.guardarPlato(plato);
    }

    private AuthUser validatePropietario(String token) {
        AuthUser authUser = authServicePort.validateToken(token);
        if (authUser == null || !Boolean.TRUE.equals(authUser.getValid())) {
            throw new TokenNoValidoException();
        }
        if (!ROL_PROPIETARIO.equalsIgnoreCase(authUser.getRol())) {
            throw new RolNoAutorizadoException();
        }
        return authUser;
    }
}
