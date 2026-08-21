package com.example.Plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser {
    private Boolean valid;
    private String correo;
    private String rol;
    private Long userId;
}
