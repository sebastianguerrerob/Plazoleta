package com.example.Plazoleta.domain.spi;

import java.util.List;
import java.util.Map;

public interface ITrazabilidadServicePort {
    void registrarCambioEstado(Long idPedido, Long idCliente, String correoCliente,
                               String estadoAnterior, String estadoNuevo,
                               Long idEmpleado, String correoEmpleado);

    List<Map<String, Object>> obtenerHistorialPorPedido(Long idPedido);

    List<Map<String, Object>> obtenerEficienciaPorRestaurante(Long idRestaurante);

    List<Map<String, Object>> obtenerRankingEmpleados(Long idRestaurante);
}
