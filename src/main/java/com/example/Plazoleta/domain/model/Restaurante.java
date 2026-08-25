package com.example.Plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurante {
    private Long id;
    private String nombre;
    private String direccion;
    private Long idPropietario;
    private String telefono;
    private String urlLogo;
    private String nit;
}
