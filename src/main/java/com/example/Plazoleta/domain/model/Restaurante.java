package com.example.Plazoleta.domain.model;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Restaurante {
    private Long id;
    private String nombre;
    private String direccion;
    private long id_propietario;
    private String telefono;
    private String urlLogo;
    private String nit;
}
