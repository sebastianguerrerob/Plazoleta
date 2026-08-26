package com.example.Plazoleta.domain.usecase;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    private PlatoUseCase platoUseCase;

    private Plato platoValido;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        platoValido = new Plato();
        platoValido.setNombre("Bandeja Paisa");
        platoValido.setIdCategoria(1L);
        platoValido.setDescripcion("Plato típico colombiano");
        platoValido.setPrecio(25000);
        platoValido.setIdRestaurante(1L);
        platoValido.setUrlImagen("https://img.com/bandeja.png");

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("Mi Restaurante");
        restaurante.setIdPropietario(10L);
    }

    @Nested
    @DisplayName("Happy Path - Creación exitosa de plato")
    class CreacionExitosa {

        @Test
        @DisplayName("Debe crear plato cuando todos los datos son válidos y el propietario es dueño")
        void crearPlato_datosValidos_guardaExitosamente() {
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L))
                    .thenReturn(Optional.of(restaurante));

            assertDoesNotThrow(() -> platoUseCase.crearPlato(platoValido, 10L));

            verify(platoPersistencePort).guardarPlato(platoValido);
        }

        @Test
        @DisplayName("Debe establecer activo en true por defecto")
        void crearPlato_nuevoPlato_activoPorDefecto() {
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L))
                    .thenReturn(Optional.of(restaurante));

            platoUseCase.crearPlato(platoValido, 10L);

            assertTrue(platoValido.getActivo());
            verify(platoPersistencePort).guardarPlato(platoValido);
        }
    }

    @Nested
    @DisplayName("Criterio 1 - Solo el propietario puede crear platos")
    class ValidacionPropietario {

        @Test
        @DisplayName("Debe lanzar excepción cuando el usuario no es dueño del restaurante")
        void crearPlato_noEsDueno_lanzaExcepcion() {
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L))
                    .thenReturn(Optional.of(restaurante));

            assertThrows(PropietarioNoEsDuenoException.class,
                    () -> platoUseCase.crearPlato(platoValido, 99L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el restaurante no existe")
        void crearPlato_restauranteNoExiste_lanzaExcepcion() {
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L))
                    .thenReturn(Optional.empty());

            assertThrows(RestauranteNoExisteException.class,
                    () -> platoUseCase.crearPlato(platoValido, 10L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }
    }

    @Nested
    @DisplayName("Criterio 2 - Validación de precio")
    class ValidacionPrecio {

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio es 0")
        void crearPlato_precioCero_lanzaExcepcion() {
            platoValido.setPrecio(0);

            assertThrows(PrecioNoValidoException.class,
                    () -> platoUseCase.crearPlato(platoValido, 10L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio es negativo")
        void crearPlato_precioNegativo_lanzaExcepcion() {
            platoValido.setPrecio(-5000);

            assertThrows(PrecioNoValidoException.class,
                    () -> platoUseCase.crearPlato(platoValido, 10L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el precio es null")
        void crearPlato_precioNull_lanzaExcepcion() {
            platoValido.setPrecio(null);

            assertThrows(PrecioNoValidoException.class,
                    () -> platoUseCase.crearPlato(platoValido, 10L));

            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe aceptar precio positivo mayor a 0")
        void crearPlato_precioPositivo_guardaExitosamente() {
            platoValido.setPrecio(1);
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L))
                    .thenReturn(Optional.of(restaurante));

            assertDoesNotThrow(() -> platoUseCase.crearPlato(platoValido, 10L));

            verify(platoPersistencePort).guardarPlato(platoValido);
        }
    }
}
