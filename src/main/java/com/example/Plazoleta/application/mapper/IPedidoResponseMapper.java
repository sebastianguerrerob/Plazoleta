package com.example.Plazoleta.application.mapper;

import com.example.Plazoleta.application.dto.PedidoPlatoResponseDto;
import com.example.Plazoleta.application.dto.PedidoResponseDto;
import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.model.PedidoPlato;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IPedidoResponseMapper {

    PedidoResponseDto toResponseDto(Pedido pedido);

    PedidoPlatoResponseDto toPlatoResponseDto(PedidoPlato pedidoPlato);

    List<PedidoResponseDto> toResponseDtoList(List<Pedido> pedidos);
}
