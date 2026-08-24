package com.example.Plazoleta.infrastructure.out.feign;

import com.example.Plazoleta.infrastructure.out.feign.dto.AuthValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-service",
        url = "${usuarios-service.url}"
)
public interface AuthFeignClient {

    @PostMapping("/auth/validate")
    AuthValidationResponse validateToken(@RequestHeader("Authorization") String authorization);
}
