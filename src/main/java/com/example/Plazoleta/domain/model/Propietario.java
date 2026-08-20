package com.example.Plazoleta.domain.model;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Propietario {

    private Long id;
    private String nombre;
    private String apellido;
    private Long rolId;
}
