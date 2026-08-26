package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IPlatoServicePort;
import com.example.Plazoleta.domain.exception.*;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PlatoUseCase implements IPlatoServicePort {

    private final IPlatoPersistencePort platoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;

    @Override
    public void crearPlato(Plato plato, Long idPropietario) {
        if (plato.getPrecio() == null || plato.getPrecio() <= 0) {
            throw new PrecioNoValidoException();
        }

        validarPropietarioRestaurante(plato.getIdRestaurante(), idPropietario);

        plato.setActivo(true);
        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public void actualizarPlato(Long idPlato, Integer precio, String descripcion, Long idPropietario) {
        Plato plato = platoPersistencePort.obtenerPlatoPorId(idPlato)
                .orElseThrow(PlatoNoExisteException::new);

        validarPropietarioRestaurante(plato.getIdRestaurante(), idPropietario);

        if (precio == null || precio <= 0) {
            throw new PrecioNoValidoException();
        }

        plato.setPrecio(precio);
        plato.setDescripcion(descripcion);

        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public void cambiarEstadoPlato(Long idPlato, Boolean activo, Long idPropietario) {
        Plato plato = platoPersistencePort.obtenerPlatoPorId(idPlato)
                .orElseThrow(PlatoNoExisteException::new);

        validarPropietarioRestaurante(plato.getIdRestaurante(), idPropietario);

        plato.setActivo(activo);
        platoPersistencePort.guardarPlato(plato);
    }

    @Override
    public PaginatedResult<Plato> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, PaginationRequest paginationRequest) {
        restaurantePersistencePort.obtenerRestaurantePorId(idRestaurante)
                .orElseThrow(RestauranteNoExisteException::new);

        return platoPersistencePort.listarPlatosPorRestaurante(idRestaurante, idCategoria, paginationRequest);
    }

    private void validarPropietarioRestaurante(Long idRestaurante, Long idPropietario) {
        Restaurante restaurante = restaurantePersistencePort.obtenerRestaurantePorId(idRestaurante)
                .orElseThrow(RestauranteNoExisteException::new);

        if (!restaurante.getIdPropietario().equals(idPropietario)) {
            throw new PropietarioNoEsDuenoException();
        }
    }
}
