package com.example.Plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plato {
    private Long id;
    private String nombre;
    private Long idCategoria;
    private String descripcion;
    private Integer precio;
    private Long idRestaurante;
    private String urlImagen;
    private Boolean activo;
}
