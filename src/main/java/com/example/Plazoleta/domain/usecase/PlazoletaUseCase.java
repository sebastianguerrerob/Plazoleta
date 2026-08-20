package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IPlazoletaServicePort;
import com.example.Plazoleta.domain.model.Propietario;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import com.example.Plazoleta.domain.spi.IUsuarioServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlazoletaUseCase implements IPlazoletaServicePort {

    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IUsuarioServicePort usuarioServicePort;
    private static final String REGEX_NUM = "\\d+";
    private static final String REGEX_CELULAR = "^\\+?\\d{1,12}$";


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
        if (!restaurante.getNit().matches(REGEX_NUM)) {
            throw new RuntimeException("El NIT debe ser únicamente numérico");
        }
        //verificar Numero
        if (!restaurante.getTelefono().matches(REGEX_CELULAR)) {
            throw new RuntimeException("Formato de celular inválido (máx 13 caracteres)");
        }
        //verificar Nombre restaurante
        if (restaurante.getNombre().matches(REGEX_NUM)) {
            throw new RuntimeException("El Nombre del restaurante no puede contener únicamente numéros");
        }

        restaurantePersistencePort.guardarRestaurante(restaurante);

    }
}
