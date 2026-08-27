package com.example.Plazoleta.domain.api;

import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Pedido;

import java.util.List;
import java.util.Map;

public interface IPedidoServicePort {
    void crearPedido(Pedido pedido);
    PaginatedResult<Pedido> listarPedidosPorEstado(Long idRestaurante, EstadoPedido estado, PaginationRequest paginationRequest);
    void asignarPedido(Long idPedido, Long idEmpleado, Long idRestaurante);
    void marcarPedidoListo(Long idPedido, Long idRestaurante);
    void entregarPedido(Long idPedido, String pin, Long idRestaurante);
    void cancelarPedido(Long idPedido, Long idCliente);
    List<Map<String, Object>> obtenerHistorialPedido(Long idPedido);
}
