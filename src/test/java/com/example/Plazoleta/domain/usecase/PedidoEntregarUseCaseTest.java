package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.DomainException;
import com.example.Plazoleta.domain.exception.PedidoNoExisteException;
import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.spi.IMensajeriaServicePort;
import com.example.Plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import com.example.Plazoleta.domain.spi.IUsuarioServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoEntregarUseCaseTest {

    @Mock
    private IPedidoPersistencePort pedidoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IUsuarioServicePort usuarioServicePort;

    @Mock
    private IMensajeriaServicePort mensajeriaServicePort;

    @InjectMocks
    private PedidoUseCase pedidoUseCase;

    private Pedido pedidoListo;

    @BeforeEach
    void setUp() {
        pedidoListo = Pedido.builder()
                .id(1L)
                .idCliente(5L)
                .idRestaurante(1L)
                .idChef(10L)
                .fecha(LocalDate.now())
                .estado(EstadoPedido.LISTO)
                .pin("4523")
                .platos(List.of())
                .build();
    }

    @Nested
    @DisplayName("Happy Path - Entrega exitosa")
    class EntregaExitosa {

        @Test
        @DisplayName("Debe cambiar estado a ENTREGADO cuando el pin es correcto")
        void entregarPedido_pinCorrecto_exitoso() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoListo));

            pedidoUseCase.entregarPedido(1L, "4523", 1L);

            assertEquals(EstadoPedido.ENTREGADO, pedidoListo.getEstado());
            verify(pedidoPersistencePort).actualizarPedido(pedidoListo);
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class Validaciones {

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no existe")
        void entregarPedido_noExiste_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(99L)).thenReturn(Optional.empty());

            assertThrows(PedidoNoExisteException.class,
                    () -> pedidoUseCase.entregarPedido(99L, "4523", 1L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no pertenece al restaurante")
        void entregarPedido_otroRestaurante_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoListo));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.entregarPedido(1L, "4523", 99L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no está en estado LISTO")
        void entregarPedido_estadoEnPreparacion_lanzaExcepcion() {
            pedidoListo.setEstado(EstadoPedido.EN_PREPARACION);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoListo));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.entregarPedido(1L, "4523", 1L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido está en estado PENDIENTE")
        void entregarPedido_estadoPendiente_lanzaExcepcion() {
            pedidoListo.setEstado(EstadoPedido.PENDIENTE);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoListo));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.entregarPedido(1L, "4523", 1L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pin no coincide")
        void entregarPedido_pinIncorrecto_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoListo));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.entregarPedido(1L, "9999", 1L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido ya fue entregado")
        void entregarPedido_yaEntregado_lanzaExcepcion() {
            pedidoListo.setEstado(EstadoPedido.ENTREGADO);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoListo));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.entregarPedido(1L, "4523", 1L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }
    }
}
