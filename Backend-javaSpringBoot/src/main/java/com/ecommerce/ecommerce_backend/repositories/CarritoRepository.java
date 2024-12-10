package com.ecommerce.ecommerce_backend.repositories;

import com.ecommerce.ecommerce_backend.models.Carrito;



import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
    Carrito findByUsuarioId(Integer idUsuario);
}