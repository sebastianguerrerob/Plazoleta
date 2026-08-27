package com.example.Plazoleta.infrastructure.out.feign;

import com.example.Plazoleta.domain.model.Propietario;
import com.example.Plazoleta.domain.spi.IUsuarioServicePort;
import com.example.Plazoleta.infrastructure.out.feign.dto.UsuarioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioFeignAdapter implements IUsuarioServicePort {

    private final UsuarioFeignClient usuarioFeignClient;

    @Override
    public Propietario obtenerUsuarioPorId(Long id) {
        UsuarioResponse usuario = usuarioFeignClient.obtenerUsuarioPorId(id);

        return new Propietario(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getRolId()
        );
    }

    @Override
    public Long obtenerRestauranteIdDeEmpleado(Long idEmpleado) {
        UsuarioResponse usuario = usuarioFeignClient.obtenerUsuarioPorId(idEmpleado);
        return usuario.getRestauranteId();
    }

    @Override
    public String obtenerTelefonoCliente(Long idCliente) {
        UsuarioResponse usuario = usuarioFeignClient.obtenerUsuarioPorId(idCliente);
        return usuario.getCelular();
    }
}
