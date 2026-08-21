package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.exception.NitNoNumericoException;
import com.example.Plazoleta.domain.exception.NombreNoValidoException;
import com.example.Plazoleta.domain.exception.PropietarioNoValidoException;
import com.example.Plazoleta.domain.exception.TelefonoNoValidoException;
import com.example.Plazoleta.domain.model.Propietario;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import com.example.Plazoleta.domain.spi.IUsuarioServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestauranteUseCase implements IRestauranteServicePort {

    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IUsuarioServicePort usuarioServicePort;
    private static final String REGEX_NUM = "\\d+";
    private static final String REGEX_CELULAR = "^\\+?\\d{1,12}$";

    @Override
    public void crearRestaurante(Restaurante restaurante) {
        // Verificar que el usuario sea propietario
        Propietario propietario = usuarioServicePort.obtenerUsuarioPorId(restaurante.getId_propietario());
        if (!propietario.getRolId().equals(2L)) {
            throw new PropietarioNoValidoException();
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
}
