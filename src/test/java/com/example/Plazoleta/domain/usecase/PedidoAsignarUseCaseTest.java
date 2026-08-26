package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.DomainException;
import com.example.Plazoleta.domain.exception.PedidoNoExisteException;
import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
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
class PedidoAsignarUseCaseTest {

    @Mock
    private IPedidoPersistencePort pedidoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

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
    @DisplayName("Happy Path - Asignación exitosa")
    class AsignacionExitosa {

        @Test
        @DisplayName("Debe asignar empleado al pedido y cambiar estado a EN_PREPARACION")
        void asignarPedido_datosValidos_asignaExitosamente() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            pedidoUseCase.asignarPedido(1L, 10L, 1L);

            assertEquals(10L, pedidoPendiente.getIdChef());
            assertEquals(EstadoPedido.EN_PREPARACION, pedidoPendiente.getEstado());
            verify(pedidoPersistencePort).actualizarPedido(pedidoPendiente);
        }
    }

    @Nested
    @DisplayName("Validaciones de asignación")
    class Validaciones {

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no existe")
        void asignarPedido_pedidoNoExiste_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(99L)).thenReturn(Optional.empty());

            assertThrows(PedidoNoExisteException.class,
                    () -> pedidoUseCase.asignarPedido(99L, 10L, 1L));
            verify(pedidoPersistencePort, never()).guardarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no pertenece al restaurante del empleado")
        void asignarPedido_otroRestaurante_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.asignarPedido(1L, 10L, 99L));
            verify(pedidoPersistencePort, never()).guardarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no está en estado PENDIENTE")
        void asignarPedido_estadoEnPreparacion_lanzaExcepcion() {
            pedidoPendiente.setEstado(EstadoPedido.EN_PREPARACION);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.asignarPedido(1L, 10L, 1L));
            verify(pedidoPersistencePort, never()).guardarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido está en estado LISTO")
        void asignarPedido_estadoListo_lanzaExcepcion() {
            pedidoPendiente.setEstado(EstadoPedido.LISTO);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoPendiente));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.asignarPedido(1L, 10L, 1L));
            verify(pedidoPersistencePort, never()).guardarPedido(any());
        }
    }
}
