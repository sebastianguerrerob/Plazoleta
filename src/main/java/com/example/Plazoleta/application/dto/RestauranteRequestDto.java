package com.example.Plazoleta.application.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@NotNull
@Getter
@Setter
public class RestauranteRequestDto {
    private String nombre;
    private String direccion;
    private long id_propietario;
    private String telefono;
    private String urlLogo;
    private String nit;

}
