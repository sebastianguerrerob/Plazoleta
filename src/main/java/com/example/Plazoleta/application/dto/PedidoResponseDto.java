package com.example.Plazoleta.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDto {
    private Long id;
    private Long idCliente;
    private LocalDate fecha;
    private String estado;
    private Long idChef;
    private Long idRestaurante;
    private List<PedidoPlatoResponseDto> platos;
}
