package com.ecommerce.ecommerce_backend.repositories;

import com.ecommerce.ecommerce_backend.models.CarritoProducto;
import com.ecommerce.ecommerce_backend.models.CarritoProductoId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, CarritoProductoId> {
    List<CarritoProducto> findByCarritoId(Integer carritoId);
    CarritoProducto findByCarritoIdAndProductoId(Integer carritoId, Integer productoId);
    void deleteByCarritoId(Integer carritoId);
}