package com.example.Plazoleta.infrastructure.out.feign;

import com.example.Plazoleta.infrastructure.out.feign.dto.UsuarioResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "usuarios-service",
        url = "${usuarios-service.url}"
)
public interface UsuarioFeignClient {

    @GetMapping("/usuarios/{id}")
    UsuarioResponse obtenerUsuarioPorId(@PathVariable("id") Long id);
}
