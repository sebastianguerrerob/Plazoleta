package com.example.Plazoleta.infrastructure.out.feign.dto;

import lombok.Data;

@Data
public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private String celular;
    private String correo;
    private Long rolId;
}