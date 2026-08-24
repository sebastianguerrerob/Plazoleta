package com.example.Plazoleta.infrastructure.out.jpa.repository;

import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PedidoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPedidoRepository extends JpaRepository<PedidoEntity, Long> {
    boolean existsByIdClienteAndEstadoIn(Long idCliente, List<EstadoPedido> estados);
}
