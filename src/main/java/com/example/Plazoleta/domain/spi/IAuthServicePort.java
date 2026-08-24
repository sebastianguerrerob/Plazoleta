package com.example.Plazoleta.domain.spi;

import com.example.Plazoleta.domain.model.AuthUser;

public interface IAuthServicePort {
    AuthUser validateToken(String token);
}
