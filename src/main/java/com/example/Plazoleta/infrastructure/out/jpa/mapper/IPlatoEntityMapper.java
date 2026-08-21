package com.example.Plazoleta.infrastructure.out.jpa.mapper;

import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PlatoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPlatoEntityMapper {
    PlatoEntity toEntity(Plato plato);
    Plato toPlato(PlatoEntity platoEntity);
}
