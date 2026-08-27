package com.example.Plazoleta.domain.usecase;

import com.example.Plazoleta.domain.api.IPedidoServicePort;
import com.example.Plazoleta.domain.exception.DomainException;
import com.example.Plazoleta.domain.exception.PedidoEnProcesoException;
import com.example.Plazoleta.domain.exception.PedidoNoExisteException;
import com.example.Plazoleta.domain.exception.RestauranteNoExisteException;
import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.spi.IMensajeriaServicePort;
import com.example.Plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import com.example.Plazoleta.domain.spi.IUsuarioServicePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IPlatoPersistencePort platoPersistencePort;
    private final IUsuarioServicePort usuarioServicePort;
    private final IMensajeriaServicePort mensajeriaServicePort;

    private static final List<EstadoPedido> ESTADOS_EN_PROCESO = List.of(
            EstadoPedido.PENDIENTE,
            EstadoPedido.EN_PREPARACION,
            EstadoPedido.LISTO
    );

    @Override
    public void crearPedido(Pedido pedido) {
        restaurantePersistencePort.obtenerRestaurantePorId(pedido.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        if (pedido.getPlatos() == null || pedido.getPlatos().isEmpty()) {
            throw new DomainException("El pedido debe contener al menos un plato");
        }

        List<Long> idPlatos = pedido.getPlatos().stream()
                .map(plato -> plato.getIdPlato())
                .toList();

        if (!platoPersistencePort.todosLosPlatosPertenecenAlRestaurante(idPlatos, pedido.getIdRestaurante())) {
            throw new DomainException("Todos los platos deben pertenecer al restaurante del pedido");
        }

        if (pedidoPersistencePort.existePedidoEnProceso(pedido.getIdCliente(), ESTADOS_EN_PROCESO)) {
            throw new PedidoEnProcesoException();
        }

        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setFecha(LocalDate.now());

        pedidoPersistencePort.guardarPedido(pedido);
    }

    @Override
    public PaginatedResult<Pedido> listarPedidosPorEstado(Long idRestaurante, EstadoPedido estado, PaginationRequest paginationRequest) {
        return pedidoPersistencePort.listarPedidosPorRestauranteYEstado(idRestaurante, estado, paginationRequest);
    }

    @Override
    public void asignarPedido(Long idPedido, Long idEmpleado, Long idRestaurante) {
        Pedido pedido = pedidoPersistencePort.obtenerPedidoPorId(idPedido)
                .orElseThrow(PedidoNoExisteException::new);

        if (!pedido.getIdRestaurante().equals(idRestaurante)) {
            throw new DomainException("El pedido no pertenece al restaurante del empleado");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new DomainException("Solo se puede asignar un pedido en estado PENDIENTE");
        }

        pedido.setIdChef(idEmpleado);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        pedidoPersistencePort.actualizarPedido(pedido);
    }

    @Override
    public void marcarPedidoListo(Long idPedido, Long idRestaurante) {
        Pedido pedido = pedidoPersistencePort.obtenerPedidoPorId(idPedido)
                .orElseThrow(PedidoNoExisteException::new);

        if (!pedido.getIdRestaurante().equals(idRestaurante)) {
            throw new DomainException("El pedido no pertenece al restaurante del empleado");
        }

        if (pedido.getEstado() != EstadoPedido.EN_PREPARACION) {
            throw new DomainException("Solo se puede marcar como listo un pedido en estado EN_PREPARACION");
        }

        // Generar pin de seguridad
        String pin = generarPin();
        pedido.setPin(pin);
        pedido.setEstado(EstadoPedido.LISTO);

        pedidoPersistencePort.actualizarPedido(pedido);

        // Obtener teléfono del cliente y notificar
        String telefono = usuarioServicePort.obtenerTelefonoCliente(pedido.getIdCliente());
        String mensaje = String.format("Su pedido #%d está listo. Pin de seguridad: %s. Presente este pin para recoger su pedido.", pedido.getId(), pin);
        mensajeriaServicePort.enviarSms(telefono, mensaje);
    }

    private String generarPin() {
        Random random = new Random();
        int pin = 1000 + random.nextInt(9000);
        return String.valueOf(pin);
    }
}
