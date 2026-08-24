package com.example.Plazoleta.application.mapper;

import com.example.Plazoleta.application.dto.PedidoPlatoRequestDto;
import com.example.Plazoleta.application.dto.PedidoRequestDto;
import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.model.PedidoPlato;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPedidoRequestMapper {

    Pedido toPedido(PedidoRequestDto pedidoRequestDto);

    PedidoPlato toPedidoPlato(PedidoPlatoRequestDto pedidoPlatoRequestDto);

    List<PedidoPlato> toPedidoPlatoList(List<PedidoPlatoRequestDto> platos);
}
