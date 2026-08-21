package com.example.Plazoleta.application.mapper;

import com.example.Plazoleta.application.dto.RestauranteRequestDto;
import com.example.Plazoleta.domain.model.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestauranteRequestMapper {

    @Mapping(target = "id",ignore = true)
    Restaurante toRestaurante(RestauranteRequestDto restauranteRequestDto);
}
