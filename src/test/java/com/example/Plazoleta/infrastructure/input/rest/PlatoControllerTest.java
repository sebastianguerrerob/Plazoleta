package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.PlatoRequestDto;
import com.example.Plazoleta.application.mapper.IPlatoRequestMapper;
import com.example.Plazoleta.application.mapper.IPlatoResponseMapper;
import com.example.Plazoleta.domain.api.IPlatoServicePort;
import com.example.Plazoleta.domain.exception.PrecioNoValidoException;
import com.example.Plazoleta.domain.exception.PropietarioNoEsDuenoException;
import com.example.Plazoleta.domain.exception.RestauranteNoExisteException;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.infrastructure.input.rest.handler.GlobalExceptionHandler;
import com.example.Plazoleta.infrastructure.input.rest.util.AuthValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlatoControllerTest {

    @Mock
    private IPlatoServicePort platoServicePort;

    @Mock
    private IPlatoRequestMapper platoRequestMapper;

    @Mock
    private IPlatoResponseMapper platoResponseMapper;

    @Mock
    private AuthValidator authValidator;

    @InjectMocks
    private PlatoController platoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(platoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        AuthUser authUser = new AuthUser(true, "prop@mail.com", "PROPIETARIO", 10L);
        when(authValidator.getAuthenticatedUser()).thenReturn(authUser);
    }

    private PlatoRequestDto crearDtoValido() {
        PlatoRequestDto dto = new PlatoRequestDto();
        dto.setNombre("Bandeja Paisa");
        dto.setIdCategoria(1L);
        dto.setDescripcion("Plato típico colombiano");
        dto.setPrecio(25000);
        dto.setIdRestaurante(1L);
        dto.setUrlImagen("https://img.com/bandeja.png");
        return dto;
    }

    @Nested
    @DisplayName("POST /Plazoleta/plato - Happy Path")
    class CreacionExitosa {

        @Test
        @DisplayName("Debe retornar 201 CREATED cuando los datos son válidos")
        void crearPlato_datosValidos_retorna201() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            when(platoRequestMapper.toPlato(any(PlatoRequestDto.class))).thenReturn(new Plato());
            doNothing().when(platoServicePort).crearPlato(any(Plato.class), eq(10L));

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());

            verify(platoServicePort).crearPlato(any(Plato.class), eq(10L));
        }
    }

    @Nested
    @DisplayName("POST /Plazoleta/plato - Validaciones DTO")
    class ValidacionesCampos {

        @Test
        @DisplayName("Debe retornar 400 cuando el nombre es vacío")
        void crearPlato_nombreVacio_retorna400() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            dto.setNombre("");

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(platoServicePort, never()).crearPlato(any(), any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando la descripción es null")
        void crearPlato_descripcionNull_retorna400() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            dto.setDescripcion(null);

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(platoServicePort, never()).crearPlato(any(), any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el precio es null")
        void crearPlato_precioNull_retorna400() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            dto.setPrecio(null);

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(platoServicePort, never()).crearPlato(any(), any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el precio es 0 (no positivo)")
        void crearPlato_precioCero_retorna400() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            dto.setPrecio(0);

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(platoServicePort, never()).crearPlato(any(), any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el id del restaurante es null")
        void crearPlato_idRestauranteNull_retorna400() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            dto.setIdRestaurante(null);

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(platoServicePort, never()).crearPlato(any(), any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando la URL de imagen es vacía")
        void crearPlato_urlImagenVacia_retorna400() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            dto.setUrlImagen("");

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(platoServicePort, never()).crearPlato(any(), any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el id de categoría es null")
        void crearPlato_idCategoriaNull_retorna400() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            dto.setIdCategoria(null);

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(platoServicePort, never()).crearPlato(any(), any());
        }
    }

    @Nested
    @DisplayName("POST /Plazoleta/plato - Excepciones de dominio")
    class ExcepcionesDominio {

        @Test
        @DisplayName("Debe retornar 403 cuando el propietario no es dueño del restaurante")
        void crearPlato_noEsDueno_retorna403() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            when(platoRequestMapper.toPlato(any(PlatoRequestDto.class))).thenReturn(new Plato());
            doThrow(new PropietarioNoEsDuenoException())
                    .when(platoServicePort).crearPlato(any(Plato.class), eq(10L));

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").value("El usuario no es propietario de este restaurante"));
        }

        @Test
        @DisplayName("Debe retornar 404 cuando el restaurante no existe")
        void crearPlato_restauranteNoExiste_retorna404() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            when(platoRequestMapper.toPlato(any(PlatoRequestDto.class))).thenReturn(new Plato());
            doThrow(new RestauranteNoExisteException())
                    .when(platoServicePort).crearPlato(any(Plato.class), eq(10L));

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("El restaurante no existe"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el precio no es válido")
        void crearPlato_precioInvalido_retorna400() throws Exception {
            PlatoRequestDto dto = crearDtoValido();
            when(platoRequestMapper.toPlato(any(PlatoRequestDto.class))).thenReturn(new Plato());
            doThrow(new PrecioNoValidoException())
                    .when(platoServicePort).crearPlato(any(Plato.class), eq(10L));

            mockMvc.perform(post("/Plazoleta/plato")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("El precio debe ser un número entero positivo mayor a 0"));
        }
    }
}
