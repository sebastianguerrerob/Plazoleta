package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.PlatoNoExisteException;
import com.example.Plazoleta.domain.exception.PrecioNoValidoException;
import com.example.Plazoleta.domain.exception.PropietarioNoEsDuenoException;
import com.example.Plazoleta.domain.exception.RestauranteNoExisteException;
import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.domain.model.Restaurante;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoUpdateUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    private PlatoUseCase platoUseCase;

    private Plato platoExistente;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        platoExistente = new Plato();
        platoExistente.setId(1L);
        platoExistente.setNombre("Bandeja Paisa");
        platoExistente.setIdCategoria(2L);
        platoExistente.setDescripcion("Descripción original");
        platoExistente.setPrecio(25000);
        platoExistente.setIdRestaurante(1L);
        platoExistente.setUrlImagen("https://img.com/bandeja.png");
        platoExistente.setActivo(true);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("Mi Restaurante");
        restaurante.setIdPropietario(10L);
    }

    @Nested
    @DisplayName("Happy Path - Actualización exitosa")
    class ActualizacionExitosa {

        @Test
        @DisplayName("Debe actualizar precio y descripción cuando el propietario es dueño")
        void actualizarPlato_datosValidos_actualizaExitosamente() {
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertDoesNotThrow(() -> platoUseCase.actualizarPlato(1L, 30000, "Nueva descripción", 10L));

            assertEquals(30000, platoExistente.getPrecio());
            assertEquals("Nueva descripción", platoExistente.getDescripcion());
            verify(platoPersistencePort).guardarPlato(platoExistente);
        }

        @Test
        @DisplayName("No debe modificar otros campos del plato")
        void actualizarPlato_soloModificaPrecioYDescripcion() {
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            platoUseCase.actualizarPlato(1L, 30000, "Nueva descripción", 10L);

            assertEquals("Bandeja Paisa", platoExistente.getNombre());
            assertEquals(2L, platoExistente.getIdCategoria());
            assertEquals(1L, platoExistente.getIdRestaurante());
            assertEquals("https://img.com/bandeja.png", platoExistente.getUrlImagen());
            assertTrue(platoExistente.getActivo());
        }
    }

    @Nested
    @DisplayName("Validaciones de actualización")
    class Validaciones {

        @Test
        @DisplayName("Debe lanzar excepción cuando el plato no existe")
        void actualizarPlato_platoNoExiste_lanzaExcepcion() {
            when(platoPersistencePort.obtenerPlatoPorId(99L)).thenReturn(Optional.empty());

            assertThrows(PlatoNoExisteException.class,
                    () -> platoUseCase.actualizarPlato(99L, 30000, "Nueva", 10L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el propietario no es dueño")
        void actualizarPlato_noEsDueno_lanzaExcepcion() {
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertThrows(PropietarioNoEsDuenoException.class,
                    () -> platoUseCase.actualizarPlato(1L, 30000, "Nueva", 99L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio es 0")
        void actualizarPlato_precioCero_lanzaExcepcion() {
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertThrows(PrecioNoValidoException.class,
                    () -> platoUseCase.actualizarPlato(1L, 0, "Nueva", 10L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio es negativo")
        void actualizarPlato_precioNegativo_lanzaExcepcion() {
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertThrows(PrecioNoValidoException.class,
                    () -> platoUseCase.actualizarPlato(1L, -5000, "Nueva", 10L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el restaurante del plato no existe")
        void actualizarPlato_restauranteNoExiste_lanzaExcepcion() {
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.empty());

            assertThrows(RestauranteNoExisteException.class,
                    () -> platoUseCase.actualizarPlato(1L, 30000, "Nueva", 10L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }
    }
}
