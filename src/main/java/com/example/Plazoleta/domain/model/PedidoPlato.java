package com.example.Plazoleta.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoPlato {
    private Long idPedido;
    private Long idPlato;
    private Integer cantidad;
}
