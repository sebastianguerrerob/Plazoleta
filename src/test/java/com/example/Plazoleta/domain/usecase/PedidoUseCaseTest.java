package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.DomainException;
import com.example.Plazoleta.domain.exception.PedidoEnProcesoException;
import com.example.Plazoleta.domain.exception.RestauranteNoExisteException;
import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.model.PedidoPlato;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoUseCaseTest {

    @Mock
    private IPedidoPersistencePort pedidoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    private PedidoUseCase pedidoUseCase;

    private Pedido pedidoValido;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        PedidoPlato plato1 = PedidoPlato.builder().idPlato(1L).cantidad(2).build();
        PedidoPlato plato2 = PedidoPlato.builder().idPlato(2L).cantidad(1).build();

        pedidoValido = Pedido.builder()
                .idCliente(5L)
                .idRestaurante(1L)
                .platos(List.of(plato1, plato2))
                .build();

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("Mi Restaurante");
    }

    @Nested
    @DisplayName("Happy Path - Creación exitosa de pedido")
    class CreacionExitosa {

        @Test
        @DisplayName("Debe crear pedido con estado PENDIENTE y fecha actual")
        void crearPedido_datosValidos_creaExitosamente() {
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));
            when(pedidoPersistencePort.existePedidoEnProceso(eq(5L), any())).thenReturn(false);

            assertDoesNotThrow(() -> pedidoUseCase.crearPedido(pedidoValido));

            assertEquals(EstadoPedido.PENDIENTE, pedidoValido.getEstado());
            assertNotNull(pedidoValido.getFecha());
            verify(pedidoPersistencePort).guardarPedido(pedidoValido);
        }
    }

    @Nested
    @DisplayName("Validaciones de pedido")
    class Validaciones {

        @Test
        @DisplayName("Debe lanzar excepción cuando el restaurante no existe")
        void crearPedido_restauranteNoExiste_lanzaExcepcion() {
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.empty());

            assertThrows(RestauranteNoExisteException.class,
                    () -> pedidoUseCase.crearPedido(pedidoValido));
            verify(pedidoPersistencePort, never()).guardarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la lista de platos es vacía")
        void crearPedido_sinPlatos_lanzaExcepcion() {
            pedidoValido.setPlatos(List.of());
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.crearPedido(pedidoValido));
            verify(pedidoPersistencePort, never()).guardarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando la lista de platos es null")
        void crearPedido_platosNull_lanzaExcepcion() {
            pedidoValido.setPlatos(null);
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertThrows(DomainException.class,
                    () -> pedidoUseCase.crearPedido(pedidoValido));
            verify(pedidoPersistencePort, never()).guardarPedido(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el cliente ya tiene un pedido en proceso")
        void crearPedido_pedidoEnProceso_lanzaExcepcion() {
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));
            when(pedidoPersistencePort.existePedidoEnProceso(eq(5L), any())).thenReturn(true);

            assertThrows(PedidoEnProcesoException.class,
                    () -> pedidoUseCase.crearPedido(pedidoValido));
            verify(pedidoPersistencePort, never()).guardarPedido(any());
        }
    }
}
