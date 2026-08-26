package com.example.Plazoleta.infrastructure.input.rest.util;

import com.example.Plazoleta.domain.exception.TokenNoValidoException;
import com.example.Plazoleta.domain.model.AuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthValidator {

    public AuthUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthUser)) {
            throw new TokenNoValidoException();
        }
        return (AuthUser) authentication.getPrincipal();
    }
}
