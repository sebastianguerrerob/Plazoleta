package com.example.Plazoleta.application.mapper;

import com.example.Plazoleta.application.dto.RestauranteResponseDto;
import com.example.Plazoleta.domain.model.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestauranteResponseMapper {

    @Mapping(source = "nombre", target = "nombre")
    @Mapping(source = "urlLogo", target = "urlLogo")
    RestauranteResponseDto toResponseDto(Restaurante restaurante);

    List<RestauranteResponseDto> toResponseDtoList(List<Restaurante> restaurantes);
}
