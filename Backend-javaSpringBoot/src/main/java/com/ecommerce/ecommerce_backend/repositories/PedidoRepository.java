package com.ecommerce.ecommerce_backend.repositories;

import com.ecommerce.ecommerce_backend.models.Pedido;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByUsuarioId(Integer usuarioId);
}