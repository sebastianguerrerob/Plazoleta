package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.PropietarioNoEsDuenoException;
import com.example.Plazoleta.domain.exception.RolNoAutorizadoException;
import com.example.Plazoleta.domain.exception.TokenNoValidoException;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.model.Plato;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IAuthServicePort;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlatoAuthUseCaseTest {

    @Mock
    private IPlatoPersistencePort platoPersistencePort;

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IAuthServicePort authServicePort;

    @InjectMocks
    private PlatoUseCase platoUseCase;

    private Plato platoValido;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        platoValido = new Plato();
        platoValido.setNombre("Bandeja Paisa");
        platoValido.setIdCategoria(1L);
        platoValido.setDescripcion("Plato típico");
        platoValido.setPrecio(25000);
        platoValido.setIdRestaurante(1L);
        platoValido.setUrlImagen("https://img.com/bandeja.png");

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("Mi Restaurante");
        restaurante.setId_propietario(10L);
    }

    @Nested
    @DisplayName("Crear plato - Validación de autenticación")
    class CrearPlatoAuth {

        @Test
        @DisplayName("Debe crear plato cuando token es válido, rol PROPIETARIO y userId es dueño")
        void crearPlato_authValida_creaExitosamente() {
            AuthUser propietario = new AuthUser(true, "prop@mail.com", "PROPIETARIO", 10L);
            when(authServicePort.validateToken("Bearer token")).thenReturn(propietario);
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertDoesNotThrow(() -> platoUseCase.crearPlato(platoValido, "Bearer token"));
            verify(platoPersistencePort).guardarPlato(platoValido);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el token es inválido")
        void crearPlato_tokenInvalido_lanzaExcepcion() {
            AuthUser invalid = new AuthUser(false, null, null, null);
            when(authServicePort.validateToken("Bearer bad")).thenReturn(invalid);

            assertThrows(TokenNoValidoException.class,
                    () -> platoUseCase.crearPlato(platoValido, "Bearer bad"));
            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el rol no es PROPIETARIO")
        void crearPlato_rolAdmin_lanzaExcepcion() {
            AuthUser admin = new AuthUser(true, "admin@mail.com", "ADMIN", 1L);
            when(authServicePort.validateToken("Bearer token")).thenReturn(admin);

            assertThrows(RolNoAutorizadoException.class,
                    () -> platoUseCase.crearPlato(platoValido, "Bearer token"));
            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el userId no es dueño del restaurante")
        void crearPlato_noEsDueno_lanzaExcepcion() {
            AuthUser otroPropietario = new AuthUser(true, "otro@mail.com", "PROPIETARIO", 99L);
            when(authServicePort.validateToken("Bearer token")).thenReturn(otroPropietario);
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertThrows(PropietarioNoEsDuenoException.class,
                    () -> platoUseCase.crearPlato(platoValido, "Bearer token"));
            verify(platoPersistencePort, never()).guardarPlato(any());
        }
    }

    @Nested
    @DisplayName("Actualizar plato - Validación de autenticación")
    class ActualizarPlatoAuth {

        @Test
        @DisplayName("Debe actualizar plato cuando token es válido y userId es dueño")
        void actualizarPlato_authValida_actualizaExitosamente() {
            Plato platoExistente = new Plato();
            platoExistente.setId(1L);
            platoExistente.setIdRestaurante(1L);
            platoExistente.setPrecio(20000);
            platoExistente.setDescripcion("Vieja");

            AuthUser propietario = new AuthUser(true, "prop@mail.com", "PROPIETARIO", 10L);
            when(authServicePort.validateToken("Bearer token")).thenReturn(propietario);
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertDoesNotThrow(() -> platoUseCase.actualizarPlato(1L, 30000, "Nueva", "Bearer token"));
            verify(platoPersistencePort).guardarPlato(platoExistente);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el token es inválido al actualizar")
        void actualizarPlato_tokenInvalido_lanzaExcepcion() {
            AuthUser invalid = new AuthUser(false, null, null, null);
            when(authServicePort.validateToken("Bearer bad")).thenReturn(invalid);

            assertThrows(TokenNoValidoException.class,
                    () -> platoUseCase.actualizarPlato(1L, 30000, "Nueva", "Bearer bad"));
            verify(platoPersistencePort, never()).guardarPlato(any());
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el userId no es dueño al actualizar")
        void actualizarPlato_noEsDueno_lanzaExcepcion() {
            Plato platoExistente = new Plato();
            platoExistente.setId(1L);
            platoExistente.setIdRestaurante(1L);

            AuthUser otroPropietario = new AuthUser(true, "otro@mail.com", "PROPIETARIO", 99L);
            when(authServicePort.validateToken("Bearer token")).thenReturn(otroPropietario);
            when(platoPersistencePort.obtenerPlatoPorId(1L)).thenReturn(Optional.of(platoExistente));
            when(restaurantePersistencePort.obtenerRestaurantePorId(1L)).thenReturn(Optional.of(restaurante));

            assertThrows(PropietarioNoEsDuenoException.class,
                    () -> platoUseCase.actualizarPlato(1L, 30000, "Nueva", "Bearer token"));
            verify(platoPersistencePort, never()).guardarPlato(any());
        }
    }
}
