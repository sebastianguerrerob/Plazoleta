package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.mapper.IRestauranteRequestMapper;
import com.example.Plazoleta.application.mapper.IRestauranteResponseMapper;
import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.spi.ITrazabilidadServicePort;
import com.example.Plazoleta.infrastructure.input.rest.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RestauranteEficienciaControllerTest {

    @Mock
    private IRestauranteServicePort restauranteServicePort;

    @Mock
    private IRestauranteRequestMapper restauranteRequestMapper;

    @Mock
    private IRestauranteResponseMapper restauranteResponseMapper;

    @Mock
    private ITrazabilidadServicePort trazabilidadServicePort;

    @InjectMocks
    private RestauranteController restauranteController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restauranteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("GET /restaurante/{id}/eficiencia - Tiempo por pedido")
    class EficienciaPorPedido {

        @Test
        @DisplayName("Debe retornar 200 con lista de eficiencia por pedido")
        void obtenerEficiencia_retornaLista() throws Exception {
            List<Map<String, Object>> eficiencia = List.of(
                    Map.of("idPedido", 3L, "tiempoMinutos", 45, "idEmpleado", 4L),
                    Map.of("idPedido", 5L, "tiempoMinutos", 30, "idEmpleado", 4L)
            );
            when(trazabilidadServicePort.obtenerEficienciaPorRestaurante(1L)).thenReturn(eficiencia);

            mockMvc.perform(get("/Plazoleta/restaurante/1/eficiencia"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].idPedido").value(3))
                    .andExpect(jsonPath("$[0].tiempoMinutos").value(45));

            verify(trazabilidadServicePort).obtenerEficienciaPorRestaurante(1L);
        }

        @Test
        @DisplayName("Debe retornar 200 con lista vacía si no hay pedidos completados")
        void obtenerEficiencia_sinResultados_retornaVacio() throws Exception {
            when(trazabilidadServicePort.obtenerEficienciaPorRestaurante(1L)).thenReturn(List.of());

            mockMvc.perform(get("/Plazoleta/restaurante/1/eficiencia"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("GET /restaurante/{id}/eficiencia/ranking - Ranking de empleados")
    class RankingEmpleados {

        @Test
        @DisplayName("Debe retornar 200 con ranking de empleados")
        void obtenerRanking_retornaLista() throws Exception {
            List<Map<String, Object>> ranking = List.of(
                    Map.of("idEmpleado", 4L, "correoEmpleado", "pedro@mail.com", "tiempoPromedioMinutos", 37.5, "pedidosCompletados", 2),
                    Map.of("idEmpleado", 6L, "correoEmpleado", "maria@mail.com", "tiempoPromedioMinutos", 50.0, "pedidosCompletados", 1)
            );
            when(trazabilidadServicePort.obtenerRankingEmpleados(1L)).thenReturn(ranking);

            mockMvc.perform(get("/Plazoleta/restaurante/1/eficiencia/ranking"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].idEmpleado").value(4))
                    .andExpect(jsonPath("$[0].tiempoPromedioMinutos").value(37.5));

            verify(trazabilidadServicePort).obtenerRankingEmpleados(1L);
        }

        @Test
        @DisplayName("Debe retornar 200 con lista vacía si no hay empleados con pedidos")
        void obtenerRanking_sinResultados_retornaVacio() throws Exception {
            when(trazabilidadServicePort.obtenerRankingEmpleados(1L)).thenReturn(List.of());

            mockMvc.perform(get("/Plazoleta/restaurante/1/eficiencia/ranking"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }
}
