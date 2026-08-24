package com.example.Plazoleta.infrastructure.out.jpa.adapter;

import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PlatoEntity;
import com.example.Plazoleta.infrastructure.out.jpa.mapper.IPlatoEntityMapper;
import com.example.Plazoleta.infrastructure.out.jpa.repository.IPlatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlatoJpaAdapter implements IPlatoPersistencePort {

    private final IPlatoRepository platoRepository;
    private final IPlatoEntityMapper platoEntityMapper;

    @Override
    public void guardarPlato(Plato plato) {
        PlatoEntity entity = platoEntityMapper.toEntity(plato);
        platoRepository.save(entity);
    }

    @Override
    public Optional<Plato> obtenerPlatoPorId(Long id) {
        return platoRepository.findById(id)
                .map(platoEntityMapper::toPlato);
    }

    @Override
    public PaginatedResult<Plato> listarPlatosPorRestaurante(Long idRestaurante, Long idCategoria, PaginationRequest paginationRequest) {
        PageRequest pageRequest = PageRequest.of(paginationRequest.getPagina(), paginationRequest.getTamano());

        Page<PlatoEntity> page;
        if (idCategoria != null) {
            page = platoRepository.findByIdRestauranteAndIdCategoria(idRestaurante, idCategoria, pageRequest);
        } else {
            page = platoRepository.findByIdRestaurante(idRestaurante, pageRequest);
        }

        List<Plato> platos = page.getContent()
                .stream()
                .map(platoEntityMapper::toPlato)
                .toList();

        return new PaginatedResult<>(
                platos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
