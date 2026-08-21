package com.example.Plazoleta.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestauranteRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "El id del propietario es obligatorio")
    private Long id_propietario;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La URL del logo es obligatoria")
    private String urlLogo;

    @NotBlank(message = "El NIT es obligatorio")
    private String nit;
}
