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
import com.example.Plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.Plazoleta.domain.spi.IPlatoPersistencePort;
import com.example.Plazoleta.domain.spi.IRestaurantePersistencePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class PedidoUseCase implements IPedidoServicePort {

    private final IPedidoPersistencePort pedidoPersistencePort;
    private final IRestaurantePersistencePort restaurantePersistencePort;
    private final IPlatoPersistencePort platoPersistencePort;

    private static final List<EstadoPedido> ESTADOS_EN_PROCESO = List.of(
            EstadoPedido.PENDIENTE,
            EstadoPedido.EN_PREPARACION,
            EstadoPedido.LISTO
    );

    @Override
    public void crearPedido(Pedido pedido) {
        // Validar que el restaurante exista
        restaurantePersistencePort.obtenerRestaurantePorId(pedido.getIdRestaurante())
                .orElseThrow(RestauranteNoExisteException::new);

        // Validar que el pedido tenga platos
        if (pedido.getPlatos() == null || pedido.getPlatos().isEmpty()) {
            throw new DomainException("El pedido debe contener al menos un plato");
        }

        // Validar que todos los platos pertenezcan al restaurante
        List<Long> idPlatos = pedido.getPlatos().stream()
                .map(plato -> plato.getIdPlato())
                .toList();

        if (!platoPersistencePort.todosLosPlatosPertenecenAlRestaurante(idPlatos, pedido.getIdRestaurante())) {
            throw new DomainException("Todos los platos deben pertenecer al restaurante del pedido");
        }

        // Validar que el cliente no tenga un pedido en proceso
        if (pedidoPersistencePort.existePedidoEnProceso(pedido.getIdCliente(), ESTADOS_EN_PROCESO)) {
            throw new PedidoEnProcesoException();
        }

        // Establecer estado inicial y fecha
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

        // Verificar que el pedido pertenezca al restaurante del empleado
        if (!pedido.getIdRestaurante().equals(idRestaurante)) {
            throw new DomainException("El pedido no pertenece al restaurante del empleado");
        }

        // Solo se puede asignar un pedido en estado PENDIENTE
        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new DomainException("Solo se puede asignar un pedido en estado PENDIENTE");
        }

        pedido.setIdChef(idEmpleado);
        pedido.setEstado(EstadoPedido.EN_PREPARACION);

        pedidoPersistencePort.actualizarPedido(pedido);
    }
}
