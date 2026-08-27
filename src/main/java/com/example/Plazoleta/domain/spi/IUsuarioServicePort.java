package com.example.Plazoleta.domain.spi;

import com.example.Plazoleta.domain.model.Propietario;

public interface IUsuarioServicePort {
    Propietario obtenerUsuarioPorId(Long id);
    Long obtenerRestauranteIdDeEmpleado(Long idEmpleado);
    String obtenerTelefonoCliente(Long idCliente);
    String obtenerCorreoCliente(Long idCliente);
    String obtenerCorreoEmpleado(Long idEmpleado);
}
