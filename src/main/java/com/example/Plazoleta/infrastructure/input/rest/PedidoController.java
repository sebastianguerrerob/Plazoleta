package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.PedidoRequestDto;
import com.example.Plazoleta.application.dto.PedidoResponseDto;
import com.example.Plazoleta.application.mapper.IPedidoRequestMapper;
import com.example.Plazoleta.application.mapper.IPedidoResponseMapper;
import com.example.Plazoleta.domain.api.IPedidoServicePort;
import com.example.Plazoleta.domain.model.*;
import com.example.Plazoleta.domain.spi.IUsuarioServicePort;
import com.example.Plazoleta.infrastructure.input.rest.util.AuthValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Plazoleta")
@RequiredArgsConstructor
public class PedidoController {

    private final IPedidoServicePort pedidoServicePort;
    private final IPedidoRequestMapper pedidoRequestMapper;
    private final IPedidoResponseMapper pedidoResponseMapper;
    private final IUsuarioServicePort usuarioServicePort;
    private final AuthValidator authValidator;

    @PostMapping("/pedido")
    public ResponseEntity<Void> crearPedido(
            @Valid @RequestBody PedidoRequestDto pedidoRequestDto) {

        AuthUser authUser = authValidator.getAuthenticatedUser();

        Pedido pedido = pedidoRequestMapper.toPedido(pedidoRequestDto);
        pedido.setIdCliente(authUser.getUserId());

        pedidoServicePort.crearPedido(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/pedidos")
    public ResponseEntity<PaginatedResult<PedidoResponseDto>> listarPedidosPorEstado(
            @RequestParam EstadoPedido estado,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        AuthUser authUser = authValidator.getAuthenticatedUser();
        Long idRestaurante = usuarioServicePort.obtenerRestauranteIdDeEmpleado(authUser.getUserId());

        PaginationRequest paginationRequest = new PaginationRequest(pagina, tamano);
        PaginatedResult<Pedido> resultado = pedidoServicePort.listarPedidosPorEstado(idRestaurante, estado, paginationRequest);

        List<PedidoResponseDto> contenidoDto = pedidoResponseMapper.toResponseDtoList(resultado.getContenido());

        PaginatedResult<PedidoResponseDto> respuesta = new PaginatedResult<>(
                contenidoDto,
                resultado.getPagina(),
                resultado.getTamano(),
                resultado.getTotalElementos(),
                resultado.getTotalPaginas()
        );

        return ResponseEntity.ok(respuesta);
    }

    @PatchMapping("/pedido/{idPedido}/asignar")
    public ResponseEntity<Void> asignarPedido(@PathVariable Long idPedido) {

        AuthUser authUser = authValidator.getAuthenticatedUser();
        Long idRestaurante = usuarioServicePort.obtenerRestauranteIdDeEmpleado(authUser.getUserId());

        pedidoServicePort.asignarPedido(idPedido, authUser.getUserId(), idRestaurante);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/pedido/{idPedido}/listo")
    public ResponseEntity<Void> marcarPedidoListo(@PathVariable Long idPedido) {

        AuthUser authUser = authValidator.getAuthenticatedUser();
        Long idRestaurante = usuarioServicePort.obtenerRestauranteIdDeEmpleado(authUser.getUserId());

        pedidoServicePort.marcarPedidoListo(idPedido, idRestaurante);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/pedido/{idPedido}/entregar")
    public ResponseEntity<Void> entregarPedido(
            @PathVariable Long idPedido,
            @RequestParam String pin) {

        AuthUser authUser = authValidator.getAuthenticatedUser();
        Long idRestaurante = usuarioServicePort.obtenerRestauranteIdDeEmpleado(authUser.getUserId());

        pedidoServicePort.entregarPedido(idPedido, pin, idRestaurante);
        return ResponseEntity.ok().build();
    }
}
