package com.example.Plazoleta.infrastructure.input.rest;

import com.example.Plazoleta.application.dto.PlatoRequestDto;
import com.example.Plazoleta.application.dto.PlatoUpdateDto;
import com.example.Plazoleta.application.mapper.IPlatoRequestMapper;
import com.example.Plazoleta.domain.api.IPlatoServicePort;
import com.example.Plazoleta.domain.model.Plato;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Plazoleta")
@RequiredArgsConstructor
public class PlatoController {

    private final IPlatoServicePort platoServicePort;
    private final IPlatoRequestMapper platoRequestMapper;

    @PostMapping("/plato")
    public ResponseEntity<Void> crearPlato(
            @Valid @RequestBody PlatoRequestDto platoRequestDto,
            @RequestHeader("Authorization") String authorization) {

        Plato plato = platoRequestMapper.toPlato(platoRequestDto);
        platoServicePort.crearPlato(plato, authorization);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/plato/{id}")
    public ResponseEntity<Void> actualizarPlato(
            @PathVariable Long id,
            @Valid @RequestBody PlatoUpdateDto platoUpdateDto,
            @RequestHeader("Authorization") String authorization) {

        platoServicePort.actualizarPlato(id, platoUpdateDto.getPrecio(), platoUpdateDto.getDescripcion(), authorization);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/plato/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoPlato(
            @PathVariable Long id,
            @RequestParam Boolean activo,
            @RequestHeader("Authorization") String authorization) {

        platoServicePort.cambiarEstadoPlato(id, activo, authorization);
        return ResponseEntity.ok().build();
    }
}
