package com.example.Plazoleta.infrastructure.out.jpa.repository;

import com.example.Plazoleta.infrastructure.out.jpa.entity.PlatoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlatoRepository extends JpaRepository<PlatoEntity, Long> {

    Page<PlatoEntity> findByIdRestaurante(Long idRestaurante, Pageable pageable);

    Page<PlatoEntity> findByIdRestauranteAndIdCategoria(Long idRestaurante, Long idCategoria, Pageable pageable);
}
