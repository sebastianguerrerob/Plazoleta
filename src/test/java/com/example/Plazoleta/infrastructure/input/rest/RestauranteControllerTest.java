package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.RestauranteRequestDto;
import com.example.Plazoleta.application.mapper.IRestauranteRequestMapper;
import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.exception.NitNoNumericoException;
import com.example.Plazoleta.domain.exception.NombreNoValidoException;
import com.example.Plazoleta.domain.exception.PropietarioNoValidoException;
import com.example.Plazoleta.domain.exception.TelefonoNoValidoException;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.infrastructure.input.rest.handler.RestauranteExceptionHandler;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RestauranteControllerTest {

    @Mock
    private IRestauranteServicePort restauranteServicePort;

    @Mock
    private IRestauranteRequestMapper restauranteRequestMapper;

    @InjectMocks
    private RestauranteController restauranteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(restauranteController)
                .setControllerAdvice(new RestauranteExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    private RestauranteRequestDto crearDtoValido() {
        RestauranteRequestDto dto = new RestauranteRequestDto();
        dto.setNombre("Mi Restaurante");
        dto.setDireccion("Calle 123");
        dto.setId_propietario(1L);
        dto.setTelefono("+573005698325");
        dto.setUrlLogo("https://logo.com/img.png");
        dto.setNit("123456789");
        return dto;
    }

    @Nested
    @DisplayName("POST /Plazoleta/restaurante - Happy Path")
    class CreacionExitosa {

        @Test
        @DisplayName("Debe retornar 201 CREATED cuando los datos son válidos")
        void crearRestaurante_datosValidos_retorna201() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            when(restauranteRequestMapper.toRestaurante(any(RestauranteRequestDto.class)))
                    .thenReturn(new Restaurante());
            doNothing().when(restauranteServicePort).crearRestaurante(any(Restaurante.class));

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());

            verify(restauranteServicePort).crearRestaurante(any(Restaurante.class));
        }
    }

    @Nested
    @DisplayName("POST /Plazoleta/restaurante - Validaciones DTO (campos obligatorios)")
    class ValidacionesCamposObligatorios {

        @Test
        @DisplayName("Debe retornar 400 cuando el nombre es vacío")
        void crearRestaurante_nombreVacio_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            dto.setNombre("");

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(restauranteServicePort, never()).crearRestaurante(any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el nombre es null")
        void crearRestaurante_nombreNull_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            dto.setNombre(null);

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(restauranteServicePort, never()).crearRestaurante(any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando la dirección es vacía")
        void crearRestaurante_direccionVacia_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            dto.setDireccion("");

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(restauranteServicePort, never()).crearRestaurante(any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el teléfono es vacío")
        void crearRestaurante_telefonoVacio_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            dto.setTelefono("");

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(restauranteServicePort, never()).crearRestaurante(any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el NIT es vacío")
        void crearRestaurante_nitVacio_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            dto.setNit("");

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(restauranteServicePort, never()).crearRestaurante(any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando la URL del logo es vacía")
        void crearRestaurante_urlLogoVacia_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            dto.setUrlLogo("");

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(restauranteServicePort, never()).crearRestaurante(any());
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el id_propietario es null")
        void crearRestaurante_idPropietarioNull_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            dto.setId_propietario(null);

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());

            verify(restauranteServicePort, never()).crearRestaurante(any());
        }
    }

    @Nested
    @DisplayName("POST /Plazoleta/restaurante - Excepciones de dominio")
    class ExcepcionesDeDominio {

        @Test
        @DisplayName("Debe retornar 403 cuando el usuario no es propietario")
        void crearRestaurante_noEsPropietario_retorna403() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            when(restauranteRequestMapper.toRestaurante(any(RestauranteRequestDto.class)))
                    .thenReturn(new Restaurante());
            doThrow(new PropietarioNoValidoException())
                    .when(restauranteServicePort).crearRestaurante(any(Restaurante.class));

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.error").value("El usuario no tiene el rol de propietario"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el NIT no es numérico")
        void crearRestaurante_nitInvalido_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            when(restauranteRequestMapper.toRestaurante(any(RestauranteRequestDto.class)))
                    .thenReturn(new Restaurante());
            doThrow(new NitNoNumericoException())
                    .when(restauranteServicePort).crearRestaurante(any(Restaurante.class));

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("El NIT debe ser únicamente numérico"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el teléfono es inválido")
        void crearRestaurante_telefonoInvalido_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            when(restauranteRequestMapper.toRestaurante(any(RestauranteRequestDto.class)))
                    .thenReturn(new Restaurante());
            doThrow(new TelefonoNoValidoException())
                    .when(restauranteServicePort).crearRestaurante(any(Restaurante.class));

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("El teléfono debe ser numérico, puede contener el símbolo + y tener máximo 13 caracteres"));
        }

        @Test
        @DisplayName("Debe retornar 400 cuando el nombre es solo números")
        void crearRestaurante_nombreSoloNumeros_retorna400() throws Exception {
            RestauranteRequestDto dto = crearDtoValido();
            when(restauranteRequestMapper.toRestaurante(any(RestauranteRequestDto.class)))
                    .thenReturn(new Restaurante());
            doThrow(new NombreNoValidoException())
                    .when(restauranteServicePort).crearRestaurante(any(Restaurante.class));

            mockMvc.perform(post("/Plazoleta/restaurante")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("El nombre del restaurante no puede contener únicamente números"));
        }
    }
}
