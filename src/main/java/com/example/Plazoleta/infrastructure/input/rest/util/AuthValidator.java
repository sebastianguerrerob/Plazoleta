package com.example.Plazoleta.infrastructure.input.rest.util;

import com.example.Plazoleta.domain.exception.RolNoAutorizadoException;
import com.example.Plazoleta.domain.exception.TokenNoValidoException;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.spi.IAuthServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthValidator {

    private final IAuthServicePort authServicePort;

    public AuthUser validateToken(String authorization) {
        AuthUser authUser = authServicePort.validateToken(authorization);
        if (authUser == null || !Boolean.TRUE.equals(authUser.getValid())) {
            throw new TokenNoValidoException();
        }
        return authUser;
    }

    public void validateRole(AuthUser authUser, String rolEsperado) {
        if (!rolEsperado.equalsIgnoreCase(authUser.getRol())) {
            throw new RolNoAutorizadoException();
        }
    }

    public AuthUser validateTokenAndRole(String authorization, String rolEsperado) {
        AuthUser authUser = validateToken(authorization);
        validateRole(authUser, rolEsperado);
        return authUser;
    }
}
