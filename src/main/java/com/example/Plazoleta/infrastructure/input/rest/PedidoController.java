package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.PedidoRequestDto;
import com.example.Plazoleta.application.mapper.IPedidoRequestMapper;
import com.example.Plazoleta.domain.api.IPedidoServicePort;
import com.example.Plazoleta.domain.exception.RolNoAutorizadoException;
import com.example.Plazoleta.domain.exception.TokenNoValidoException;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.spi.IAuthServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Plazoleta")
@RequiredArgsConstructor
public class PedidoController {

    private final IPedidoServicePort pedidoServicePort;
    private final IPedidoRequestMapper pedidoRequestMapper;
    private final IAuthServicePort authServicePort;

    private static final String ROL_CLIENTE = "CLIENTE";

    @PostMapping("/pedido")
    public ResponseEntity<Void> crearPedido(
            @Valid @RequestBody PedidoRequestDto pedidoRequestDto,
            @RequestHeader("Authorization") String authorization) {

        AuthUser authUser = validateToken(authorization);
        validateRole(authUser, ROL_CLIENTE);

        Pedido pedido = pedidoRequestMapper.toPedido(pedidoRequestDto);
        pedido.setIdCliente(authUser.getUserId());

        pedidoServicePort.crearPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private AuthUser validateToken(String authorization) {
        AuthUser authUser = authServicePort.validateToken(authorization);
        if (authUser == null || !Boolean.TRUE.equals(authUser.getValid())) {
            throw new TokenNoValidoException();
        }
        return authUser;
    }

    private void validateRole(AuthUser authUser, String rolEsperado) {
        if (!rolEsperado.equalsIgnoreCase(authUser.getRol())) {
            throw new RolNoAutorizadoException();
        }
    }
}
