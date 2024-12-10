package com.ecommerce.ecommerce_backend.services;

import com.ecommerce.ecommerce_backend.models.Producto;
import com.ecommerce.ecommerce_backend.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // Método para obtener la lista de productos
    public List<Producto> getListaProductos() {
        return productoRepository.findAll(); // Esto devolverá todos los productos, incluyendo las URLs de las imágenes
    }

    // Método para crear un nuevo producto
    public Producto createProducto(Producto producto) {
        // Aquí podrías agregar validaciones adicionales si es necesario
        return productoRepository.save(producto); // Guardamos el nuevo producto en la base de datos
    }

    // Método para editar un producto existente
    public Producto editProducto(Integer id, Producto productoDetails) {
        // Buscar el producto por su ID
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        // Actualizamos los detalles del producto con la información nueva
        producto.setNombre(productoDetails.getNombre());
        producto.setDescripcion(productoDetails.getDescripcion());
        producto.setPrecio(productoDetails.getPrecio());
        producto.setStock(productoDetails.getStock());
        producto.setImagen_url(productoDetails.getImagen_url()); // Asegúrate de que el nombre del método coincide

        // Guardamos y devolvemos el producto actualizado
        return productoRepository.save(producto);
    }

    // Método para eliminar un producto
    public void deleteProducto(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        // Lo eliminamos de la base de datos
        productoRepository.delete(producto);
    }
}