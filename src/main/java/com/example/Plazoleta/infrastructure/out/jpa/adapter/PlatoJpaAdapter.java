package com.example.Plazoleta.infrastructure.out.jpa.adapter;

import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PlatoEntity;
import com.example.Plazoleta.infrastructure.out.jpa.mapper.IPlatoEntityMapper;
import com.example.Plazoleta.infrastructure.out.jpa.repository.IPlatoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
