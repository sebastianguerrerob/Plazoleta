package com.example.Plazoleta.domain.spi;

import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface IPedidoPersistencePort {
    void guardarPedido(Pedido pedido);
    boolean existePedidoEnProceso(Long idCliente, List<EstadoPedido> estadosEnProceso);
    PaginatedResult<Pedido> listarPedidosPorRestauranteYEstado(Long idRestaurante, EstadoPedido estado, PaginationRequest paginationRequest);
    Optional<Pedido> obtenerPedidoPorId(Long id);
}
