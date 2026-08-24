package com.example.Plazoleta.infrastructure.out.feign;

import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.spi.IAuthServicePort;
import com.example.Plazoleta.infrastructure.out.feign.dto.AuthValidationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFeignAdapter implements IAuthServicePort {

    private final AuthFeignClient authFeignClient;

    @Override
    public AuthUser validateToken(String token) {
        AuthValidationResponse response = authFeignClient.validateToken(token);
        return new AuthUser(
                response.getValid(),
                response.getCorreo(),
                response.getRol(),
                response.getUserId()
        );
    }
}
