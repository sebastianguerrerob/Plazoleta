package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.NitNoNumericoException;
import com.example.Plazoleta.domain.exception.NombreNoValidoException;
import com.example.Plazoleta.domain.exception.TelefonoNoValidoException;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
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

    @InjectMocks
    private RestauranteUseCase restauranteUseCase;

    private Restaurante restauranteValido;

    @BeforeEach
    void setUp() {
        restauranteValido = new Restaurante();
        restauranteValido.setNombre("Mi Restaurante");
        restauranteValido.setDireccion("Calle 123");
        restauranteValido.setIdPropietario(1L);
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
            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, times(1)).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar teléfono sin símbolo +")
        void crearRestaurante_telefonoSinMas_guardaExitosamente() {
            restauranteValido.setTelefono("3005698325");

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar nombre con números y letras")
        void crearRestaurante_nombreConNumerosYLetras_guardaExitosamente() {
            restauranteValido.setNombre("Restaurante 123");

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }

    @Nested
    @DisplayName("Validación NIT y Teléfono")
    class ValidacionNitYTelefono {

        @Test
        @DisplayName("Debe lanzar excepción cuando el NIT contiene letras")
        void crearRestaurante_nitConLetras_lanzaExcepcion() {
            restauranteValido.setNit("123ABC456");

            assertThrows(NitNoNumericoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el NIT contiene caracteres especiales")
        void crearRestaurante_nitConCaracteresEspeciales_lanzaExcepcion() {
            restauranteValido.setNit("123-456-789");

            assertThrows(NitNoNumericoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el teléfono excede 13 caracteres")
        void crearRestaurante_telefonoMuyLargo_lanzaExcepcion() {
            restauranteValido.setTelefono("+5730056983251");

            assertThrows(TelefonoNoValidoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el teléfono contiene letras")
        void crearRestaurante_telefonoConLetras_lanzaExcepcion() {
            restauranteValido.setTelefono("+57300ABC");

            assertThrows(TelefonoNoValidoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe aceptar teléfono con exactamente 13 caracteres incluyendo +")
        void crearRestaurante_telefono13Caracteres_guardaExitosamente() {
            restauranteValido.setTelefono("+573005698325");

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }

    @Nested
    @DisplayName("Validación nombre del restaurante")
    class ValidacionNombre {

        @Test
        @DisplayName("Debe lanzar excepción cuando el nombre contiene solo números")
        void crearRestaurante_nombreSoloNumeros_lanzaExcepcion() {
            restauranteValido.setNombre("12345");

            assertThrows(NombreNoValidoException.class,
                    () -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort, never()).guardarRestaurante(any());
        }

        @Test
        @DisplayName("Debe aceptar nombre que es solo letras")
        void crearRestaurante_nombreSoloLetras_guardaExitosamente() {
            restauranteValido.setNombre("Restaurante");

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }

        @Test
        @DisplayName("Debe aceptar nombre con números mezclados con letras")
        void crearRestaurante_nombreMixto_guardaExitosamente() {
            restauranteValido.setNombre("Burger 53");

            assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido));
            verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
        }
    }
}
