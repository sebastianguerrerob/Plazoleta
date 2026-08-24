package com.example.Plazoleta.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequestDto {

    @NotNull(message = "El id del restaurante es obligatorio")
    private Long idRestaurante;

    @NotEmpty(message = "El pedido debe contener al menos un plato")
    @Valid
    private List<PedidoPlatoRequestDto> platos;
}
