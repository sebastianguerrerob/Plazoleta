package com.example.Plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {
    private Long id;
    private Long idCliente;
    private LocalDate fecha;
    private EstadoPedido estado;
    private Long idChef;
    private Long idRestaurante;
    private List<PedidoPlato> platos;
}
