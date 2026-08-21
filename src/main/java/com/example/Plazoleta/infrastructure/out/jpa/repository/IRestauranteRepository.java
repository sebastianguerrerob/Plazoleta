package com.example.Plazoleta.infrastructure.out.jpa.repository;

import com.example.Plazoleta.infrastructure.out.jpa.entity.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRestauranteRepository extends JpaRepository<RestauranteEntity, Long> {
}
