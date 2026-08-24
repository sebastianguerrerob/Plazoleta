package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.PlatoNoExisteException;
import com.example.Plazoleta.domain.exception.PropietarioNoEsDuenoException;
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
class PlatoEstadoUseCaseTest {

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
        platoExistente.setIdRestaurante(1L);
        platoExistente.setPrecio(25000);
        platoExistente.setActivo(true);

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("Mi Restaurante");
        restaurante.setId_propietario(10L);
    }

    @Nested
    @DisplayName("Happy Path - Cambio de estado exitoso")
    class CambioExitoso {

        @Test
        @DisplayName("Debe desactivar un plato activo")
        void cambiarEstado_desactivar_exitoso() {
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            platoUseCase.cambiarEstadoPlato(1L, false, 10L);

            assertFalse(platoExistente.getActivo());
            verify(platoPersistencePort).guardarPlato(platoExistente);
        }

        @Test
        @DisplayName("Debe activar un plato inactivo")
        void cambiarEstado_activar_exitoso() {
            platoExistente.setActivo(false);
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            platoUseCase.cambiarEstadoPlato(1L, true, 10L);

            assertTrue(platoExistente.getActivo());
            verify(platoPersistencePort).guardarPlato(platoExistente);
        }
    }

    @Nested
    @DisplayName("Criterio 1 - Solo el propietario puede habilitar/deshabilitar")
    class ValidacionPropietario {

        @Test
        @DisplayName("Debe lanzar excepción cuando el plato no existe")
        void cambiarEstado_platoNoExiste_lanzaExcepcion() {
            when(platoPersistencePort.obtenerPlatoPorId(99L)).thenReturn(Optional.empty());

            assertThrows(PlatoNoExisteException.class,
                    () -> platoUseCase.cambiarEstadoPlato(99L, false, 10L));
            verify(platoPersistencePort, never()).guardarPlato(any());
        }
    }

    @Nested
    @DisplayName("Criterio 2 - No se permiten modificar platos de otros restaurantes")
    class ValidacionOtroRestaurante {

        @Test
        @DisplayName("Debe lanzar excepción cuando el userId no es dueño del restaurante")
        void cambiarEstado_noEsDueno_lanzaExcepcion() {
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertThrows(PropietarioNoEsDuenoException.class,
                    () -> platoUseCase.cambiarEstadoPlato(1L, false, 99L));
            verify(platoPersistencePort, never()).guardarPlato(any());
        }
    }
}
