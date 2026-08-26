package com.example.Plazoleta.infrastructure.out.jpa.repository;

import com.example.Plazoleta.domain.model.EstadoPedido;
import com.example.Plazoleta.infrastructure.out.jpa.entity.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPedidoRepository extends JpaRepository<PedidoEntity, Long> {
    boolean existsByIdClienteAndEstadoIn(Long idCliente, List<EstadoPedido> estados);
    Page<PedidoEntity> findByIdRestauranteAndEstado(Long idRestaurante, EstadoPedido estado, Pageable pageable);

    @Query("SELECT p FROM PedidoEntity p LEFT JOIN FETCH p.platos WHERE p.id = :id")
    Optional<PedidoEntity> findByIdWithPlatos(@Param("id") Long id);
}
