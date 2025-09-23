package com.jmj.restaurante.repository;

import com.jmj.restaurante.model.Pedido;
import com.jmj.restaurante.model.enums.PedidoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByStatus(PedidoStatus status);
}
