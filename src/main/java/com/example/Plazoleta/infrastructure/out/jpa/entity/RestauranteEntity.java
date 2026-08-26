package com.example.Plazoleta.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurantes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RestauranteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String direccion;
    @Column(name = "id_propietario")
    private Long idPropietario;
    private String telefono;
    private String urlLogo;
    @Column(unique = true, nullable = false)
    private String nit;
}
