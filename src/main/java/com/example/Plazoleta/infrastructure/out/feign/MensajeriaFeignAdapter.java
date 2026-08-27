package com.example.Plazoleta.infrastructure.out.feign;

import com.example.Plazoleta.domain.spi.IMensajeriaServicePort;
import com.example.Plazoleta.infrastructure.out.feign.dto.SmsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MensajeriaFeignAdapter implements IMensajeriaServicePort {

    private final MensajeriaFeignClient mensajeriaFeignClient;

    @Override
    public void enviarSms(String telefono, String mensaje) {
        mensajeriaFeignClient.enviarSms(new SmsRequest(telefono, mensaje));
    }
}
