package com.example.Plazoleta.infrastructure.out.feign;

import com.example.Plazoleta.infrastructure.out.feign.dto.SmsRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "mensajeria-service",
        url = "${mensajeria-service.url}"
)
public interface MensajeriaFeignClient {

    @PostMapping("/mensajeria/sms")
    void enviarSms(@RequestBody SmsRequest smsRequest);
}
