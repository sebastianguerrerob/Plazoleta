package com.example.Plazoleta.infrastructure.out.jpa.mapper;

import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.model.PedidoPlato;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PedidoEntity;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PedidoPlatoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPedidoEntityMapper {

    PedidoEntity toEntity(Pedido pedido);

    PedidoPlatoEntity toPlatoEntity(PedidoPlato pedidoPlato);

    List<PedidoPlatoEntity> toPlatoEntityList(List<PedidoPlato> platos);
}
