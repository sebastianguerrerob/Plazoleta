package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.RestauranteNoExisteException;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoListarUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    private PlatoUseCase platoUseCase;

    @Nested
    @DisplayName("Happy Path - Listar platos")
    class ListarExitoso {

        @Test
        @DisplayName("Debe listar platos de un restaurante sin filtro de categoría")
        void listarPlatos_sinCategoria_retornaResultado() {
            Restaurante restaurante = new Restaurante();
            restaurante.setId(1L);
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            List<Plato> platos = List.of(
                    Plato.builder().id(1L).nombre("Bandeja").idCategoria(2L).precio(25000).idRestaurante(1L).activo(true).build(),
                    Plato.builder().id(2L).nombre("Ajiaco").idCategoria(3L).precio(18000).idRestaurante(1L).activo(true).build()
            );
            PaginatedResult<Plato> expected = new PaginatedResult<>(platos, 0, 10, 2, 1);
            PaginationRequest request = new PaginationRequest(0, 10);

            when(platoPersistencePort.listarPlatosPorRestaurante(eq(1L), eq(null), any(PaginationRequest.class)))
                    .thenReturn(expected);

            PaginatedResult<Plato> resultado = platoUseCase.listarPlatosPorRestaurante(1L, null, request);

            assertEquals(2, resultado.getContenido().size());
            assertEquals(0, resultado.getPagina());
            assertEquals(2, resultado.getTotalElementos());
            verify(platoPersistencePort).listarPlatosPorRestaurante(1L, null, request);
        }

        @Test
        @DisplayName("Debe listar platos filtrados por categoría")
        void listarPlatos_conCategoria_retornaFiltrado() {
            Restaurante restaurante = new Restaurante();
            restaurante.setId(1L);
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            List<Plato> platos = List.of(
                    Plato.builder().id(1L).nombre("Bandeja").idCategoria(2L).precio(25000).idRestaurante(1L).activo(true).build()
            );
            PaginatedResult<Plato> expected = new PaginatedResult<>(platos, 0, 10, 1, 1);
            PaginationRequest request = new PaginationRequest(0, 10);

            when(platoPersistencePort.listarPlatosPorRestaurante(eq(1L), eq(2L), any(PaginationRequest.class)))
                    .thenReturn(expected);

            PaginatedResult<Plato> resultado = platoUseCase.listarPlatosPorRestaurante(1L, 2L, request);

            assertEquals(1, resultado.getContenido().size());
            assertEquals(2L, resultado.getContenido().get(0).getIdCategoria());
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay platos")
        void listarPlatos_sinResultados_retornaVacio() {
            Restaurante restaurante = new Restaurante();
            restaurante.setId(1L);
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            PaginatedResult<Plato> expected = new PaginatedResult<>(List.of(), 0, 10, 0, 0);
            PaginationRequest request = new PaginationRequest(0, 10);

            when(platoPersistencePort.listarPlatosPorRestaurante(eq(1L), eq(null), any(PaginationRequest.class)))
                    .thenReturn(expected);

            PaginatedResult<Plato> resultado = platoUseCase.listarPlatosPorRestaurante(1L, null, request);

            assertTrue(resultado.getContenido().isEmpty());
            assertEquals(0, resultado.getTotalElementos());
        }
    }

    @Nested
    @DisplayName("Validaciones")
    class Validaciones {

        @Test
        @DisplayName("Debe lanzar excepción cuando el restaurante no existe")
        void listarPlatos_restauranteNoExiste_lanzaExcepcion() {
            when(restaurantePersistencePort.obtenerRestaurantePorId(99L)).thenReturn(Optional.empty());
            PaginationRequest request = new PaginationRequest(0, 10);

            assertThrows(RestauranteNoExisteException.class,
                    () -> platoUseCase.listarPlatosPorRestaurante(99L, null, request));

            verify(platoPersistencePort, never()).listarPlatosPorRestaurante(any(), any(), any());
        }
    }
}
