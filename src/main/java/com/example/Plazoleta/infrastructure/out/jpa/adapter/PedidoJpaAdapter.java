package com.example.Plazoleta.infrastructure.out.jpa.adapter;

import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.domain.model.PaginatedResult;
import com.example.Plazoleta.domain.model.PaginationRequest;
import com.example.Plazoleta.domain.model.Pedido;
import com.example.Plazoleta.domain.spi.IPedidoPersistencePort;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PedidoEntity;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PedidoPlatoEntity;
import com.example.Plazoleta.infrastructure.out.jpa.mapper.IPedidoEntityMapper;
import com.example.Plazoleta.infrastructure.out.jpa.repository.IPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PedidoJpaAdapter implements IPedidoPersistencePort {

    private final IPedidoRepository pedidoRepository;
    private final IPedidoEntityMapper pedidoEntityMapper;

    @Override
    public void guardarPedido(Pedido pedido) {
        PedidoEntity pedidoEntity = pedidoEntityMapper.toEntity(pedido);
        List<PedidoPlatoEntity> platosEntity = pedidoEntityMapper.toPlatoEntityList(pedido.getPlatos());

        platosEntity.forEach(platoEntity -> platoEntity.setPedido(pedidoEntity));
        pedidoEntity.setPlatos(platosEntity);

        pedidoRepository.save(pedidoEntity);
    }

    @Override
    public void actualizarPedido(Pedido pedido) {
        pedidoRepository.findById(pedido.getId()).ifPresent(entity -> {
            entity.setEstado(pedido.getEstado());
            entity.setIdChef(pedido.getIdChef());
            pedidoRepository.save(entity);
        });
    }

    @Override
    public boolean existePedidoEnProceso(Long idCliente, List<EstadoPedido> estadosEnProceso) {
        return pedidoRepository.existsByIdClienteAndEstadoIn(idCliente, estadosEnProceso);
    }

    @Override
    public PaginatedResult<Pedido> listarPedidosPorRestauranteYEstado(Long idRestaurante, EstadoPedido estado, PaginationRequest paginationRequest) {
        PageRequest pageRequest = PageRequest.of(paginationRequest.getPagina(), paginationRequest.getTamano());
        Page<PedidoEntity> page = pedidoRepository.findByIdRestauranteAndEstado(idRestaurante, estado, pageRequest);

        List<Pedido> pedidos = page.getContent().stream()
                .map(entity -> {
                    Pedido pedido = pedidoEntityMapper.toPedido(entity);
                    pedido.setPlatos(pedidoEntityMapper.toPedidoPlatoList(entity.getPlatos()));
                    return pedido;
                })
                .toList();

        return new PaginatedResult<>(
                pedidos,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public Optional<Pedido> obtenerPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(entity -> {
                    Pedido pedido = pedidoEntityMapper.toPedido(entity);
                    pedido.setPlatos(pedidoEntityMapper.toPedidoPlatoList(entity.getPlatos()));
                    return pedido;
                });
    }
}
