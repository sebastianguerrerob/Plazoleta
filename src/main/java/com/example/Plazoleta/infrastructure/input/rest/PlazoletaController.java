package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.RestauranteRequestDto;
import com.example.Plazoleta.application.mapper.IRestauranteRequestMapper;
import com.example.Plazoleta.domain.api.IPlazoletaServicePort;
import com.example.Plazoleta.domain.model.Restaurante;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Plazoleta")
@RequiredArgsConstructor
public class PlazoletaController {
    private final IPlazoletaServicePort plazoletaServicePort;
    private final IRestauranteRequestMapper restauranteRequestMapper;

    @PostMapping("/restaurante")
    public ResponseEntity<Void> crearRestaurante(@RequestBody RestauranteRequestDto restauranteRequestDto) {

        plazoletaServicePort.crearRestaurante(restauranteRequestMapper.toRestaurante(restauranteRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
