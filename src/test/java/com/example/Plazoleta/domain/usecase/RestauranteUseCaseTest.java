package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.NitNoNumericoException;
import com.example.Plazoleta.domain.exception.NombreNoValidoException;
import com.example.Plazoleta.domain.exception.PropietarioNoValidoException;
import com.example.Plazoleta.domain.exception.TelefonoNoValidoException;
import com.example.Plazoleta.domain.model.Propietario;
import com.example.Plazoleta.domain.model.Restaurante;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestauranteUseCaseTest {

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IUsuarioServicePort usuarioServicePort;

    @InjectMocks
    private RestauranteUseCase restauranteUseCase;

    private Restaurante restauranteValido;

    @BeforeEach
    void setUp() {
        restauranteValido = new Restaurante();
        restauranteValido.setNombre("Mi Restaurante");
        restauranteValido.setDireccion("Calle 123");
        restauranteValido.setId_propietario(1L);
        restauranteValido.setTelefono("+573005698325");
        restauranteValido.setUrlLogo("https://logo.com/img.png");
        restauranteValido.setNit("123456789");
    }

    @Nested
    @DisplayName("Happy Path - Creación exitosa")
    class CreacionExitosa {

        @Test
        @DisplayName("Debe crear restaurante cuando todos los datos son válidos")
        void crearRestaurante_datosValidos_guardaExitosamente() {
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));

            verify(restaurantePersistencePort, times(1)).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar teléfono sin símbolo +")
        void crearRestaurante_telefonoSinMas_guardaExitosamente() {
            restauranteValido.setTelefono("3005698325");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar nombre con números y letras")
        void crearRestaurante_nombreConNumerosYLetras_guardaExitosamente() {
            restauranteValido.setNombre("Restaurante 123");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }

    @Nested
    @DisplayName("Criterio 2 - Validación de rol propietario")
    class ValidacionRolPropietario {

        @Test
        @DisplayName("Debe lanzar excepción cuando el usuario no tiene rol de propietario")
        void crearRestaurante_usuarioNoEsPropietario_lanzaExcepcion() {
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 3L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertThrows(PropietarioNoValidoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el usuario tiene rol de administrador (rol 1)")
        void crearRestaurante_usuarioEsAdmin_lanzaExcepcion() {
            Propietario propietario = new Propietario(1L, "Admin", "Admin", 1L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertThrows(PropietarioNoValidoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }
    }

    @Nested
    @DisplayName("Criterio 3 - Validación NIT y Teléfono")
    class ValidacionNitYTelefono {

        @Test
        @DisplayName("Debe lanzar excepción cuando el NIT contiene letras")
        void crearRestaurante_nitConLetras_lanzaExcepcion() {
            restauranteValido.setNit("123ABC456");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertThrows(NitNoNumericoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el NIT contiene caracteres especiales")
        void crearRestaurante_nitConCaracteresEspeciales_lanzaExcepcion() {
            restauranteValido.setNit("123-456-789");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertThrows(NitNoNumericoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el teléfono excede 13 caracteres")
        void crearRestaurante_telefonoMuyLargo_lanzaExcepcion() {
            restauranteValido.setTelefono("+5730056983251");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertThrows(TelefonoNoValidoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el teléfono contiene letras")
        void crearRestaurante_telefonoConLetras_lanzaExcepcion() {
            restauranteValido.setTelefono("+57300ABC");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertThrows(TelefonoNoValidoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe aceptar teléfono con exactamente 13 caracteres incluyendo +")
        void crearRestaurante_telefono13Caracteres_guardaExitosamente() {
            restauranteValido.setTelefono("+573005698325");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }

    @Nested
    @DisplayName("Criterio 4 - Validación nombre del restaurante")
    class ValidacionNombre {

        @Test
        @DisplayName("Debe lanzar excepción cuando el nombre contiene solo números")
        void crearRestaurante_nombreSoloNumeros_lanzaExcepcion() {
            restauranteValido.setNombre("12345");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertThrows(NombreNoValidoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe aceptar nombre que es solo letras")
        void crearRestaurante_nombreSoloLetras_guardaExitosamente() {
            restauranteValido.setNombre("Restaurante");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar nombre con números mezclados con letras")
        void crearRestaurante_nombreMixto_guardaExitosamente() {
            restauranteValido.setNombre("Burger 53");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }
}
