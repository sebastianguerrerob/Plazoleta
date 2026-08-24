package com.example.Plazoleta.infrastructure.out.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pedidos_platos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoPlatoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private PedidoEntity pedido;

    @Column(name = "id_plato")
    private Long idPlato;

    private Integer cantidad;
}
