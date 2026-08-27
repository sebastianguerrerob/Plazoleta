package com.example.Plazoleta.infrastructure.out.jpa.entity;

import com.example.Plazoleta.domain.model.EstadoPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_cliente")
    private Long idCliente;

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Column(name = "id_chef")
    private Long idChef;

    @Column(name = "id_restaurante")
    private Long idRestaurante;

    private String pin;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoPlatoEntity> platos;
}
