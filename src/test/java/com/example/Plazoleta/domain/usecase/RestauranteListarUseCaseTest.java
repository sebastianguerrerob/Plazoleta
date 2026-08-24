package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.DomainException;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteListarUseCaseTest {

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @InjectMocks
    private RestauranteUseCase restauranteUseCase;

    @Nested
    @DisplayName("Happy Path - Listar restaurantes")
    class ListarExitoso {

        @Test
        @DisplayName("Debe retornar lista paginada de restaurantes")
        void listarRestaurantes_paginaValida_retornaResultado() {
            List<Restaurante> restaurantes = List.of(
                    new Restaurante(1L, "Burger 53", "Calle 1", 2L, "300123", "url1", "123"),
                    new Restaurante(2L, "El Corral", "Calle 2", 2L, "300456", "url2", "456")
            );
            PaginatedResult<Restaurante> expected = new PaginatedResult<>(restaurantes, 0, 10, 2, 1);
            PaginationRequest request = new PaginationRequest(0, 10);

            when(restaurantePersistencePort.listarRestaurantesOrdenadosPorNombre(any(PaginationRequest.class)))
                    .thenReturn(expected);

            PaginatedResult<Restaurante> resultado = restauranteUseCase.listarRestaurantes(request);

            assertEquals(2, resultado.getContenido().size());
            assertEquals(0, resultado.getPagina());
            assertEquals(10, resultado.getTamano());
            assertEquals(2, resultado.getTotalElementos());
            assertEquals(1, resultado.getTotalPaginas());
            verify(restaurantePersistencePort).listarRestaurantesOrdenadosPorNombre(request);
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay restaurantes")
        void listarRestaurantes_sinResultados_retornaVacio() {
            PaginatedResult<Restaurante> expected = new PaginatedResult<>(List.of(), 0, 10, 0, 0);
            PaginationRequest request = new PaginationRequest(0, 10);

            when(restaurantePersistencePort.listarRestaurantesOrdenadosPorNombre(any(PaginationRequest.class)))
                    .thenReturn(expected);

            PaginatedResult<Restaurante> resultado = restauranteUseCase.listarRestaurantes(request);

            assertTrue(resultado.getContenido().isEmpty());
            assertEquals(0, resultado.getTotalElementos());
        }
    }

    @Nested
    @DisplayName("Validación de PaginationRequest")
    class ValidacionPaginacion {

        @Test
        @DisplayName("Debe lanzar excepción cuando la página es negativa")
        void paginationRequest_paginaNegativa_lanzaExcepcion() {
            assertThrows(DomainException.class, () -> new PaginationRequest(-1, 10));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el tamaño es 0")
        void paginationRequest_tamanoCero_lanzaExcepcion() {
            assertThrows(DomainException.class, () -> new PaginationRequest(0, 0));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el tamaño es negativo")
        void paginationRequest_tamanoNegativo_lanzaExcepcion() {
            assertThrows(DomainException.class, () -> new PaginationRequest(0, -5));
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el tamaño excede el máximo (50)")
        void paginationRequest_tamanoExcedeMaximo_lanzaExcepcion() {
            assertThrows(DomainException.class, () -> new PaginationRequest(0, 51));
        }

        @Test
        @DisplayName("Debe crear correctamente con tamaño máximo permitido (50)")
        void paginationRequest_tamanoMaximo_creaExitosamente() {
            PaginationRequest request = new PaginationRequest(0, 50);
            assertEquals(50, request.getTamano());
        }

        @Test
        @DisplayName("Debe crear correctamente con valores válidos")
        void paginationRequest_valoresValidos_creaExitosamente() {
            PaginationRequest request = new PaginationRequest(2, 15);
            assertEquals(2, request.getPagina());
            assertEquals(15, request.getTamano());
        }
    }
}
