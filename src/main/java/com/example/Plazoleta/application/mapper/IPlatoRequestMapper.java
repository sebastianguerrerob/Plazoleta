package com.example.Plazoleta.application.mapper;

import com.example.Plazoleta.application.dto.PlatoRequestDto;
import com.example.Plazoleta.domain.model.Plato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlatoRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    Plato toPlato(PlatoRequestDto platoRequestDto);
}
