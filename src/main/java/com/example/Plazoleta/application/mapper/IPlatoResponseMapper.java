package com.example.Plazoleta.application.mapper;

import com.example.Plazoleta.application.dto.PlatoResponseDto;
import com.example.Plazoleta.domain.model.Plato;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlatoResponseMapper {

    PlatoResponseDto toResponseDto(Plato plato);

    List<PlatoResponseDto> toResponseDtoList(List<Plato> platos);
}
