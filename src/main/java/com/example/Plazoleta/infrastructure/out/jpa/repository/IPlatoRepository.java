package com.example.Plazoleta.infrastructure.out.jpa.repository;

import com.example.Plazoleta.infrastructure.out.jpa.entity.PlatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlatoRepository extends JpaRepository<PlatoEntity, Long> {
}
