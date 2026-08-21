package com.example.Plazoleta.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoRequestDto {

    @NotBlank(message = "El nombre del plato es obligatorio")
    private String nombre;

    @NotNull(message = "El id de la categoría es obligatorio")
    private Long idCategoria;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un número positivo mayor a 0")
    private Integer precio;

    @NotNull(message = "El id del restaurante es obligatorio")
    private Long idRestaurante;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    private String urlImagen;
}
