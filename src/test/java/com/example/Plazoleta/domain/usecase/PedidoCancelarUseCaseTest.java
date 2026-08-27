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
class PedidoCancelarUseCaseTest {

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

    private Pedido pedidoPendiente;

    @BeforeEach
    void setUp() {
        pedidoPendiente = Pedido.builder()
                .id(1L)
                .idCliente(5L)
                .idRestaurante(1L)
                .fecha(LocalDate.now())
                .estado(EstadoPedido.PENDIENTE)
                .platos(List.of())
                .build();
    }

    @Nested
    @DisplayName("Happy Path - Cancelación exitosa")
    class CancelacionExitosa {

        @Test
        @DisplayName("Debe cancelar pedido cuando está en estado PENDIENTE y es del cliente")
        void cancelarPedido_pendiente_exitoso() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            pedidoUseCase.cancelarPedido(1L, 5L);

            assertEquals(EstadoPedido.CANCELADO, pedidoPendiente.getEstado());
            verify(pedidoPersistencePort).actualizarPedido(pedidoPendiente);
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class Validaciones {

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no existe")
        void cancelarPedido_noExiste_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(99L)).thenReturn(Optional.empty());

            assertThrows(PedidoNoExisteException.class,
                    () -> pedidoUseCase.cancelarPedido(99L, 5L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no pertenece al cliente")
        void cancelarPedido_otroCliente_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.cancelarPedido(1L, 99L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción con mensaje específico cuando pedido está EN_PREPARACION")
        void cancelarPedido_enPreparacion_lanzaExcepcionConMensaje() {
            pedidoPendiente.setEstado(EstadoPedido.EN_PREPARACION);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            DomainException ex = assertThrows(DomainException.class,
                    () -> pedidoUseCase.cancelarPedido(1L, 5L));
            assertEquals("Lo sentimos, tu pedido ya está en preparación y no puede cancelarse", ex.getMessage());
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando pedido está LISTO")
        void cancelarPedido_listo_lanzaExcepcion() {
            pedidoPendiente.setEstado(EstadoPedido.LISTO);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.cancelarPedido(1L, 5L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando pedido ya fue ENTREGADO")
        void cancelarPedido_entregado_lanzaExcepcion() {
            pedidoPendiente.setEstado(EstadoPedido.ENTREGADO);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.cancelarPedido(1L, 5L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
        }
    }
}
