package com.example.Plazoleta.infrastructure.out.feign;

import com.example.Plazoleta.infrastructure.out.feign.dto.TrazabilidadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient(
        name = "trazabilidad-service",
        url = "${trazabilidad-service.url}"
)
public interface TrazabilidadFeignClient {

    @PostMapping("/trazabilidad")
    void registrarCambioEstado(@RequestBody TrazabilidadRequest request);

    @GetMapping("/trazabilidad/pedido/{idPedido}")
    List<Map<String, Object>> obtenerHistorialPorPedido(@PathVariable("idPedido") Long idPedido);
}
