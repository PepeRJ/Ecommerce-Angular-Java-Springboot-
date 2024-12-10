package com.ecommerce.ecommerce_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Map;

@Entity
@Table(name = "carrito_productos")
public class CarritoProducto {

    @EmbeddedId
    private CarritoProductoId id;

    @Column(nullable = false)
    private Integer cantidad = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCarrito")
    @JoinColumn(name = "id_carrito")
    @JsonIgnore
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto")
    private Producto producto;

    public CarritoProductoId getId() {
        return id;
    }

    public void setId(CarritoProductoId id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @JsonProperty("producto")
    public Map<String, Object> getProductoInfo() {
        return Map.of(
            "id", producto.getId(),
            "nombre", producto.getNombre(),
            "precio", producto.getPrecio()
        );
    }
}