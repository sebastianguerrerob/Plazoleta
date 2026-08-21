package com.example.Plazoleta.infrastructure.out.jpa.mapper;

import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.infrastructure.out.jpa.entity.RestauranteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestauranteEntityMapper {
        RestauranteEntity toEntity(Restaurante restaurante);
        Restaurante toRestaurante(RestauranteEntity restauranteEntity);
}
