package com.example.Plazoleta.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "platos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlatoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(name = "id_categoria")
    private Long idCategoria;

    private String descripcion;

    private Integer precio;

    @Column(name = "id_restaurante")
    private Long idRestaurante;

    @Column(name = "url_imagen")
    private String urlImagen;

    private Boolean activo;
}
