package com.example.Plazoleta.infrastructure.out.jpa.adapter;

import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import com.example.Plazoleta.infrastructure.out.jpa.entity.RestauranteEntity;
import com.example.Plazoleta.infrastructure.out.jpa.mapper.IRestauranteEntityMapper;
import com.example.Plazoleta.infrastructure.out.jpa.repository.IRestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestauranteJpaAdapter implements IRestaurantePersistencePort {

    private final IRestauranteRepository restauranteRepository;
    private final IRestauranteEntityMapper restauranteEntityMapper;

    @Override
    public void guardarRestaurante(Restaurante restaurante) {
        RestauranteEntity entity = restauranteEntityMapper.toEntity(restaurante);
        restauranteRepository.save(entity);
    }

    @Override
    public Optional<Restaurante> obtenerRestaurantePorId(Long id) {
        return restauranteRepository.findById(id)
                .map(restauranteEntityMapper::toRestaurante);
    }
}
