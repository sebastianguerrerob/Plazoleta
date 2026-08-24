package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.PlatoRequestDto;
import com.example.Plazoleta.application.dto.PlatoResponseDto;
import com.example.Plazoleta.application.dto.PlatoUpdateDto;
import com.example.Plazoleta.application.mapper.IPlatoRequestMapper;
import com.example.Plazoleta.application.mapper.IPlatoResponseMapper;
import com.example.Plazoleta.domain.api.IPlatoServicePort;
import com.example.Plazoleta.domain.exception.RolNoAutorizadoException;
import com.example.Plazoleta.domain.exception.TokenNoValidoException;
import com.example.Plazoleta.domain.model.AuthUser;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Plato;
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
public class PlatoController {

    private final IPlatoServicePort platoServicePort;
    private final IPlatoRequestMapper platoRequestMapper;
    private final IPlatoResponseMapper platoResponseMapper;
    private final IAuthServicePort authServicePort;

    private static final String ROL_PROPIETARIO = "PROPIETARIO";

    @PostMapping("/plato")
    public ResponseEntity<Void> crearPlato(
            @Valid @RequestBody PlatoRequestDto platoRequestDto,
            @RequestHeader("Authorization") String authorization) {

        AuthUser authUser = validateToken(authorization);
        validateRole(authUser, ROL_PROPIETARIO);

        Plato plato = platoRequestMapper.toPlato(platoRequestDto);
        platoServicePort.crearPlato(plato, authUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/plato/{id}")
    public ResponseEntity<Void> actualizarPlato(
            @PathVariable Long id,
            @Valid @RequestBody PlatoUpdateDto platoUpdateDto,
            @RequestHeader("Authorization") String authorization) {

        AuthUser authUser = validateToken(authorization);
        validateRole(authUser, ROL_PROPIETARIO);

        platoServicePort.actualizarPlato(id, platoUpdateDto.getPrecio(), platoUpdateDto.getDescripcion(), authUser.getUserId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/plato/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoPlato(
            @PathVariable Long id,
            @RequestParam Boolean activo,
            @RequestHeader("Authorization") String authorization) {

        AuthUser authUser = validateToken(authorization);
        validateRole(authUser, ROL_PROPIETARIO);

        platoServicePort.cambiarEstadoPlato(id, activo, authUser.getUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/restaurante/{idRestaurante}/platos")
    public ResponseEntity<PaginatedResult<PlatoResponseDto>> listarPlatosPorRestaurante(
            @PathVariable Long idRestaurante,
            @RequestParam(required = false) Long idCategoria,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamano) {

        PaginationRequest paginationRequest = new PaginationRequest(pagina, tamano);
        PaginatedResult<Plato> resultado = platoServicePort.listarPlatosPorRestaurante(idRestaurante, idCategoria, paginationRequest);

        List<PlatoResponseDto> contenidoDto = platoResponseMapper.toResponseDtoList(resultado.getContenido());

        PaginatedResult<PlatoResponseDto> respuesta = new PaginatedResult<>(
                contenidoDto,
                resultado.getPagina(),
                resultado.getTamano(),
                resultado.getTotalElementos(),
                resultado.getTotalPaginas()
        );

        return ResponseEntity.ok(respuesta);
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
