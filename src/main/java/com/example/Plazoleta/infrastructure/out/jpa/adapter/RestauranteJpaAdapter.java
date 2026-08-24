package com.example.Plazoleta.infrastructure.out.jpa.adapter;

import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import com.example.Plazoleta.infrastructure.out.jpa.entity.RestauranteEntity;
import com.example.Plazoleta.infrastructure.out.jpa.mapper.IRestauranteEntityMapper;
import com.example.Plazoleta.infrastructure.out.jpa.repository.IRestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
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

    @Override
    public PaginatedResult<Restaurante> listarRestaurantesOrdenadosPorNombre(PaginationRequest paginationRequest) {
        PageRequest pageRequest = PageRequest.of(paginationRequest.getPagina(), paginationRequest.getTamano(), Sort.by(Sort.Direction.ASC, "nombre"));
        Page<RestauranteEntity> page = restauranteRepository.findAll(pageRequest);

        List<Restaurante> restaurantes = page.getContent()
                .stream()
                .map(restauranteEntityMapper::toRestaurante)
                .toList();

        return new PaginatedResult<>(
                restaurantes,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
