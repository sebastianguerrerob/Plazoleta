package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.RestauranteRequestDto;
import com.example.Plazoleta.application.dto.RestauranteResponseDto;
import com.example.Plazoleta.application.mapper.IRestauranteRequestMapper;
import com.example.Plazoleta.application.mapper.IRestauranteResponseMapper;
import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.exception.RolNoAutorizadoException;
import com.example.Plazoleta.domain.exception.TokenNoValidoException;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Restaurante;
import com.example.Plazoleta.domain.spi.IAuthServicePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Plazoleta")
@RequiredArgsConstructor
public class RestauranteController {

    private final IRestauranteServicePort restauranteServicePort;
    private final IRestauranteRequestMapper restauranteRequestMapper;
    private final IRestauranteResponseMapper restauranteResponseMapper;
    private final IAuthServicePort authServicePort;

    private static final String ROL_ADMIN = "ADMIN";

    @PostMapping("/restaurante")
    public ResponseEntity<Void> crearRestaurante(
            @Valid @RequestBody RestauranteRequestDto restauranteRequestDto,
            @RequestHeader("Authorization") String authorization) {

        AuthUser authUser = validateToken(authorization);
        validateRole(authUser, ROL_ADMIN);

        restauranteServicePort.crearRestaurante(
                restauranteRequestMapper.toRestaurante(restauranteRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/restaurantes")
    public ResponseEntity<PaginatedResult<RestauranteResponseDto>> listarRestaurantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        PaginationRequest paginationRequest = new PaginationRequest(pagina, tamano);
        PaginatedResult<Restaurante> resultado = restauranteServicePort.listarRestaurantes(paginationRequest);

        List<RestauranteResponseDto> contenidoDto = restauranteResponseMapper.toResponseDtoList(resultado.getContenido());

        PaginatedResult<RestauranteResponseDto> respuesta = new PaginatedResult<>(
                contenidoDto,
                resultado.getPagina(),
                resultado.getTamano(),
                resultado.getTotalElementos(),
                resultado.getTotalPaginas()
        );

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/restaurante/{idRestaurante}/propietario/{idPropietario}/validar")
    public ResponseEntity<java.util.Map<String, Boolean>> validarPropietario(
            @PathVariable Long idRestaurante,
            @PathVariable Long idPropietario) {

        boolean esPropietario = restauranteServicePort.validarPropietarioRestaurante(idRestaurante, idPropietario);
        return ResponseEntity.ok(java.util.Map.of("esPropietario", esPropietario));
    }

    private AuthUser validateToken(String authorization) {
        AuthUser authUser = authServicePort.validateToken(authorization);
        if (authUser == null || !Boolean.TRUE.equals(authUser.getValid())) {
            throw new TokenNoValidoException();
        }
        return authUser;
    }

    private void validateRole(AuthUser authUser, String rolEsperado) {
        if (!rolEsperado.equalsIgnoreCase(authUser.getRol())) {
            throw new RolNoAutorizadoException();
        }
    }
}
