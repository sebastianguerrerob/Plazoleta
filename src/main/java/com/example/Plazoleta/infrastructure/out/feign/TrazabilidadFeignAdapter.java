package com.example.Plazoleta.infrastructure.out.feign;

import com.example.Plazoleta.domain.spi.ITrazabilidadServicePort;
import com.example.Plazoleta.infrastructure.out.feign.dto.TrazabilidadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TrazabilidadFeignAdapter implements ITrazabilidadServicePort {

    private final TrazabilidadFeignClient trazabilidadFeignClient;

    @Override
    public void registrarCambioEstado(Long idPedido, Long idCliente, String correoCliente,
                                      String estadoAnterior, String estadoNuevo,
                                      Long idEmpleado, String correoEmpleado) {
        TrazabilidadRequest request = TrazabilidadRequest.builder()
                .idPedido(idPedido)
                .idCliente(idCliente)
                .correoCliente(correoCliente)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .idEmpleado(idEmpleado)
                .correoEmpleado(correoEmpleado)
                .build();

        trazabilidadFeignClient.registrarCambioEstado(request);
    }

    @Override
    public List<Map<String, Object>> obtenerHistorialPorPedido(Long idPedido) {
        return trazabilidadFeignClient.obtenerHistorialPorPedido(idPedido);
    }

    @Override
    public List<Map<String, Object>> obtenerEficienciaPorRestaurante(Long idRestaurante) {
        return trazabilidadFeignClient.obtenerEficienciaPorRestaurante(idRestaurante);
    }

    @Override
    public List<Map<String, Object>> obtenerRankingEmpleados(Long idRestaurante) {
        return trazabilidadFeignClient.obtenerRankingEmpleados(idRestaurante);
    }
}
