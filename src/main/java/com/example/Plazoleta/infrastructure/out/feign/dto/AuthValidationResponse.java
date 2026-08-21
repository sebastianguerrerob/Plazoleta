package com.example.Plazoleta.infrastructure.out.feign.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthValidationResponse {
    private Boolean valid;
    private String correo;
    private String rol;
    private Long userId;
}
