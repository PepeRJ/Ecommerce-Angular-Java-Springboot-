package com.ecommerce.ecommerce_backend.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CarritoProductoId implements Serializable {
    
    private Integer idCarrito;
    private Integer idProducto;

    // Constructor por defecto
    public CarritoProductoId() {}

    public CarritoProductoId(Integer idCarrito, Integer idProducto) {
        this.idCarrito = idCarrito;
        this.idProducto = idProducto;
    }

    // Getters y Setters
    public Integer getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(Integer idCarrito) {
        this.idCarrito = idCarrito;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarritoProductoId that = (CarritoProductoId) o;
        return Objects.equals(idCarrito, that.idCarrito) &&
               Objects.equals(idProducto, that.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCarrito, idProducto);
    }
}