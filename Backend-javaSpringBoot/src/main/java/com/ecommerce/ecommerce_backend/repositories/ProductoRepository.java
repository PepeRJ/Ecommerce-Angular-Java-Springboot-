package com.ecommerce.ecommerce_backend.repositories;

import com.ecommerce.ecommerce_backend.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
}