package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.model.*;
import com.example.Plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoListarUseCaseTest {

    @Mock
    private IPedidoPersistencePort pedidoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    private PedidoUseCase pedidoUseCase;

    @Nested
    @DisplayName("Happy Path - Listar pedidos por estado")
    class ListarExitoso {

        @Test
        @DisplayName("Debe listar pedidos pendientes de un restaurante")
        void listarPedidos_estadoPendiente_retornaResultado() {
            List<Pedido> pedidos = List.of(
                    Pedido.builder()
                            .id(1L).idCliente(3L).idRestaurante(1L)
                            .fecha(LocalDate.now()).estado(EstadoPedido.PENDIENTE)
                            .platos(List.of(PedidoPlato.builder().idPlato(1L).cantidad(2).build()))
                            .build(),
                    Pedido.builder()
                            .id(2L).idCliente(5L).idRestaurante(1L)
                            .fecha(LocalDate.now()).estado(EstadoPedido.PENDIENTE)
                            .platos(List.of(PedidoPlato.builder().idPlato(3L).cantidad(1).build()))
                            .build()
            );
            PaginatedResult<Pedido> expected = new PaginatedResult<>(pedidos, 0, 10, 2, 1);
            PaginationRequest request = new PaginationRequest(0, 10);

            when(pedidoPersistencePort.listarPedidosPorRestauranteYEstado(eq(1L), eq(EstadoPedido.PENDIENTE), any(PaginationRequest.class)))
                    .thenReturn(expected);

            PaginatedResult<Pedido> resultado = pedidoUseCase.listarPedidosPorEstado(1L, EstadoPedido.PENDIENTE, request);

            assertEquals(2, resultado.getContenido().size());
            assertEquals(EstadoPedido.PENDIENTE, resultado.getContenido().get(0).getEstado());
            verify(pedidoPersistencePort).listarPedidosPorRestauranteYEstado(1L, EstadoPedido.PENDIENTE, request);
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay pedidos con ese estado")
        void listarPedidos_sinResultados_retornaVacio() {
            PaginatedResult<Pedido> expected = new PaginatedResult<>(List.of(), 0, 10, 0, 0);
            PaginationRequest request = new PaginationRequest(0, 10);

            when(pedidoPersistencePort.listarPedidosPorRestauranteYEstado(eq(1L), eq(EstadoPedido.EN_PREPARACION), any(PaginationRequest.class)))
                    .thenReturn(expected);

            PaginatedResult<Pedido> resultado = pedidoUseCase.listarPedidosPorEstado(1L, EstadoPedido.EN_PREPARACION, request);

            assertTrue(resultado.getContenido().isEmpty());
            assertEquals(0, resultado.getTotalElementos());
        }

        @Test
        @DisplayName("Debe listar pedidos listos de un restaurante")
        void listarPedidos_estadoListo_retornaResultado() {
            List<Pedido> pedidos = List.of(
                    Pedido.builder()
                            .id(3L).idCliente(6L).idRestaurante(1L)
                            .fecha(LocalDate.now()).estado(EstadoPedido.LISTO).idChef(4L)
                            .platos(List.of(PedidoPlato.builder().idPlato(2L).cantidad(1).build()))
                            .build()
            );
            PaginatedResult<Pedido> expected = new PaginatedResult<>(pedidos, 0, 10, 1, 1);
            PaginationRequest request = new PaginationRequest(0, 10);

            when(pedidoPersistencePort.listarPedidosPorRestauranteYEstado(eq(1L), eq(EstadoPedido.LISTO), any(PaginationRequest.class)))
                    .thenReturn(expected);

            PaginatedResult<Pedido> resultado = pedidoUseCase.listarPedidosPorEstado(1L, EstadoPedido.LISTO, request);

            assertEquals(1, resultado.getContenido().size());
            assertEquals(EstadoPedido.LISTO, resultado.getContenido().get(0).getEstado());
            assertEquals(4L, resultado.getContenido().get(0).getIdChef());
        }
    }
}
