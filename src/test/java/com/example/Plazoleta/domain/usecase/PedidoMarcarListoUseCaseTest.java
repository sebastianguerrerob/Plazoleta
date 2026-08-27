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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoMarcarListoUseCaseTest {

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

    private Pedido pedidoEnPreparacion;

    @BeforeEach
    void setUp() {
        pedidoEnPreparacion = Pedido.builder()
                .id(1L)
                .idCliente(5L)
                .idRestaurante(1L)
                .idChef(10L)
                .fecha(LocalDate.now())
                .estado(EstadoPedido.EN_PREPARACION)
                .platos(List.of())
                .build();
    }

    @Nested
    @DisplayName("Happy Path - Marcar pedido como listo")
    class MarcarListoExitoso {

        @Test
        @DisplayName("Debe cambiar estado a LISTO, generar pin y enviar SMS")
        void marcarListo_datosValidos_exitoso() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoEnPreparacion));
            when(usuarioServicePort.obtenerTelefonoCliente(5L)).thenReturn("+573005698325");

            pedidoUseCase.marcarPedidoListo(1L, 1L);

            assertEquals(EstadoPedido.LISTO, pedidoEnPreparacion.getEstado());
            assertNotNull(pedidoEnPreparacion.getPin());
            assertEquals(4, pedidoEnPreparacion.getPin().length());
            verify(pedidoPersistencePort).actualizarPedido(pedidoEnPreparacion);
            verify(mensajeriaServicePort).enviarSms(eq("+573005698325"), anyString());
        }

        @Test
        @DisplayName("El pin generado debe ser numérico de 4 dígitos")
        void marcarListo_pinGenerado_es4Digitos() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoEnPreparacion));
            when(usuarioServicePort.obtenerTelefonoCliente(5L)).thenReturn("+573005698325");

            pedidoUseCase.marcarPedidoListo(1L, 1L);

            String pin = pedidoEnPreparacion.getPin();
            assertTrue(pin.matches("\\d{4}"));
            int pinNumero = Integer.parseInt(pin);
            assertTrue(pinNumero >= 1000 && pinNumero <= 9999);
        }

        @Test
        @DisplayName("El mensaje SMS debe contener el id del pedido y el pin")
        void marcarListo_mensajeSms_contieneInfoCorrecta() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoEnPreparacion));
            when(usuarioServicePort.obtenerTelefonoCliente(5L)).thenReturn("+573005698325");

            pedidoUseCase.marcarPedidoListo(1L, 1L);

            verify(mensajeriaServicePort).enviarSms(
                    eq("+573005698325"),
                    argThat(mensaje -> mensaje.contains("#1") && mensaje.contains(pedidoEnPreparacion.getPin()))
            );
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class Validaciones {

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no existe")
        void marcarListo_pedidoNoExiste_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(99L)).thenReturn(Optional.empty());

            assertThrows(PedidoNoExisteException.class,
                    () -> pedidoUseCase.marcarPedidoListo(99L, 1L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
            verify(mensajeriaServicePort, never()).enviarSms(any(), any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no pertenece al restaurante")
        void marcarListo_otroRestaurante_lanzaExcepcion() {
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoEnPreparacion));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.marcarPedidoListo(1L, 99L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
            verify(mensajeriaServicePort, never()).enviarSms(any(), any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido no está en estado EN_PREPARACION")
        void marcarListo_estadoPendiente_lanzaExcepcion() {
            pedidoEnPreparacion.setEstado(EstadoPedido.PENDIENTE);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoEnPreparacion));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.marcarPedidoListo(1L, 1L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
            verify(mensajeriaServicePort, never()).enviarSms(any(), any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el pedido ya está LISTO")
        void marcarListo_estadoListo_lanzaExcepcion() {
            pedidoEnPreparacion.setEstado(EstadoPedido.LISTO);
            when(pedidoPersistencePort.obtenerPedidoPorId(1L)).thenReturn(Optional.of(pedidoEnPreparacion));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.marcarPedidoListo(1L, 1L));
            verify(pedidoPersistencePort, never()).actualizarPedido(any());
            verify(mensajeriaServicePort, never()).enviarSms(any(), any());
        }
    }
}
