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
class PlazoletaUseCaseTest {

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IUsuarioServicePort usuarioServicePort;

    @InjectMocks
    private PlazoletaUseCase plazoletaUseCase;

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
            // Arrange
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act
            assertDoesNotThrow(() -> plazoletaUseCase.crearRestaurante(restauranteValido));

            // Assert
            verify(restaurantePersistencePort, times(1)).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar teléfono sin símbolo +")
        void crearRestaurante_telefonoSinMas_guardaExitosamente() {
            // Arrange
            restauranteValido.setTelefono("3005698325");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertDoesNotThrow(() -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar nombre con números y letras")
        void crearRestaurante_nombreConNumerosYLetras_guardaExitosamente() {
            // Arrange
            restauranteValido.setNombre("Restaurante 123");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertDoesNotThrow(() -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }

    @Nested
    @DisplayName("Criterio 2 - Validación de rol propietario")
    class ValidacionRolPropietario {

        @Test
        @DisplayName("Debe lanzar excepción cuando el usuario no tiene rol de propietario")
        void crearRestaurante_usuarioNoEsPropietario_lanzaExcepcion() {
            // Arrange - rol 3 (no es propietario)
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 3L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertThrows(PropietarioNoValidoException.class,
                    () -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el usuario tiene rol de administrador (rol 1)")
        void crearRestaurante_usuarioEsAdmin_lanzaExcepcion() {
            // Arrange
            Propietario propietario = new Propietario(1L, "Admin", "Admin", 1L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertThrows(PropietarioNoValidoException.class,
                    () -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }
    }

    @Nested
    @DisplayName("Criterio 3 - Validación NIT y Teléfono")
    class ValidacionNitYTelefono {

        @Test
        @DisplayName("Debe lanzar excepción cuando el NIT contiene letras")
        void crearRestaurante_nitConLetras_lanzaExcepcion() {
            // Arrange
            restauranteValido.setNit("123ABC456");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertThrows(NitNoNumericoException.class,
                    () -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el NIT contiene caracteres especiales")
        void crearRestaurante_nitConCaracteresEspeciales_lanzaExcepcion() {
            // Arrange
            restauranteValido.setNit("123-456-789");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertThrows(NitNoNumericoException.class,
                    () -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el teléfono excede 13 caracteres")
        void crearRestaurante_telefonoMuyLargo_lanzaExcepcion() {
            // Arrange
            restauranteValido.setTelefono("+5730056983251"); // 14 caracteres
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertThrows(TelefonoNoValidoException.class,
                    () -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el teléfono contiene letras")
        void crearRestaurante_telefonoConLetras_lanzaExcepcion() {
            // Arrange
            restauranteValido.setTelefono("+57300ABC");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertThrows(TelefonoNoValidoException.class,
                    () -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe aceptar teléfono con exactamente 13 caracteres incluyendo +")
        void crearRestaurante_telefono13Caracteres_guardaExitosamente() {
            // Arrange
            restauranteValido.setTelefono("+573005698325"); // 13 caracteres
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertDoesNotThrow(() -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }

    @Nested
    @DisplayName("Criterio 4 - Validación nombre del restaurante")
    class ValidacionNombre {

        @Test
        @DisplayName("Debe lanzar excepción cuando el nombre contiene solo números")
        void crearRestaurante_nombreSoloNumeros_lanzaExcepcion() {
            // Arrange
            restauranteValido.setNombre("12345");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertThrows(NombreNoValidoException.class,
                    () -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe aceptar nombre que es solo letras")
        void crearRestaurante_nombreSoloLetras_guardaExitosamente() {
            // Arrange
            restauranteValido.setNombre("Restaurante");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertDoesNotThrow(() -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar nombre con números mezclados con letras")
        void crearRestaurante_nombreMixto_guardaExitosamente() {
            // Arrange
            restauranteValido.setNombre("Burger 53");
            Propietario propietario = new Propietario(1L, "Juan", "Perez", 2L);
            when(usuarioServicePort.obtenerUsuarioPorId(1L)).thenReturn(propietario);

            // Act & Assert
            assertDoesNotThrow(() -> plazoletaUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }
}
