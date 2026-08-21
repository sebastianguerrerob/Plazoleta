package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.RestauranteRequestDto;
import com.example.Plazoleta.application.mapper.IRestauranteRequestMapper;
import com.example.Plazoleta.domain.api.IRestauranteServicePort;
import jakarta.validation.Valid;
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
public class RestauranteController {

    private final IRestauranteServicePort restauranteServicePort;
    private final IRestauranteRequestMapper restauranteRequestMapper;

    @PostMapping("/restaurante")
    public ResponseEntity<Void> crearRestaurante(@Valid @RequestBody RestauranteRequestDto restauranteRequestDto) {
        restauranteServicePort.crearRestaurante(restauranteRequestMapper.toRestaurante(restauranteRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
