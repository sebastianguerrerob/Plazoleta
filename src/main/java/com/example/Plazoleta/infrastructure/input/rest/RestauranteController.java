package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.RestauranteRequestDto;
import com.example.Plazoleta.application.dto.RestauranteResponseDto;
import com.example.Plazoleta.application.mapper.IRestauranteRequestMapper;
import com.example.Plazoleta.application.mapper.IRestauranteResponseMapper;
import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Restaurante;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/Plazoleta")
@RequiredArgsConstructor
public class RestauranteController {

    private final IRestauranteServicePort restauranteServicePort;
    private final IRestauranteRequestMapper restauranteRequestMapper;
    private final IRestauranteResponseMapper restauranteResponseMapper;

    @PostMapping("/restaurante")
    public ResponseEntity<Void> crearRestaurante(
            @Valid @RequestBody RestauranteRequestDto restauranteRequestDto) {

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
    public ResponseEntity<Map<String, Boolean>> validarPropietario(
            @PathVariable Long idRestaurante,
            @PathVariable Long idPropietario) {

        boolean esPropietario = restauranteServicePort.validarPropietarioRestaurante(idRestaurante, idPropietario);
        return ResponseEntity.ok(Map.of("esPropietario", esPropietario));
    }
}
