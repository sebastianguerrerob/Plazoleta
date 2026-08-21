package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.exception.RolNoAutorizadoException;
import com.example.Plazoleta.domain.exception.TokenNoValidoException;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IAuthServicePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class RestauranteAuthUseCaseTest {

    @Mock
    private IRestaurantePersistencePort restaurantePersistencePort;

    @Mock
    private IAuthServicePort authServicePort;

    @InjectMocks
    private RestauranteUseCase restauranteUseCase;

    private Restaurante restauranteValido;

    @BeforeEach
    void setUp() {
        restauranteValido = new Restaurante();
        restauranteValido.setNombre("Mi Restaurante");
        restauranteValido.setDireccion("Calle 123");
        restauranteValido.setId_propietario(2L);
        restauranteValido.setTelefono("+573005698325");
        restauranteValido.setUrlLogo("https://logo.com/img.png");
        restauranteValido.setNit("123456789");
    }

    @Test
    @DisplayName("Debe crear restaurante cuando el token es válido y rol es ADMIN")
    void crearRestaurante_tokenValidoRolAdmin_creaExitosamente() {
        AuthUser admin = new AuthUser(true, "admin@mail.com", "ADMIN", 1L);
        when(authServicePort.validateToken("Bearer token123")).thenReturn(admin);

        assertDoesNotThrow(() -> restauranteUseCase.crearRestaurante(restauranteValido, "Bearer token123"));
        verify(restaurantePersistencePort).guardarRestaurante(restauranteValido);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el token es inválido")
    void crearRestaurante_tokenInvalido_lanzaExcepcion() {
        AuthUser invalidUser = new AuthUser(false, null, null, null);
        when(authServicePort.validateToken("Bearer bad")).thenReturn(invalidUser);

        assertThrows(TokenNoValidoException.class,
                () -> restauranteUseCase.crearRestaurante(restauranteValido, "Bearer bad"));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el token retorna null")
    void crearRestaurante_tokenNull_lanzaExcepcion() {
        when(authServicePort.validateToken("Bearer null")).thenReturn(null);

        assertThrows(TokenNoValidoException.class,
                () -> restauranteUseCase.crearRestaurante(restauranteValido, "Bearer null"));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el rol no es ADMIN")
    void crearRestaurante_rolPropietario_lanzaExcepcion() {
        AuthUser propietario = new AuthUser(true, "prop@mail.com", "PROPIETARIO", 2L);
        when(authServicePort.validateToken("Bearer token")).thenReturn(propietario);

        assertThrows(RolNoAutorizadoException.class,
                () -> restauranteUseCase.crearRestaurante(restauranteValido, "Bearer token"));
        verify(restaurantePersistencePort, never()).guardarRestaurante(any());
    }
}
