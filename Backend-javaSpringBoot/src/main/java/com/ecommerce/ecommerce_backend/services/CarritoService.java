package com.ecommerce.ecommerce_backend.services;

import com.ecommerce.ecommerce_backend.models.*;
import com.ecommerce.ecommerce_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public void agregarProductoAlCarrito(Integer idUsuario, Integer idProducto, Integer cantidad) {
        Carrito carrito = carritoRepository.findByUsuarioId(idUsuario);
        
        if (carrito == null) {
            Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
            carrito = new Carrito();
            carrito.setUsuario(usuario);
            carrito.setCantidad(0);
            carritoRepository.save(carrito);
            System.out.println("Se ha creado un nuevo carrito para el usuario ID: " + idUsuario);
        }

        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        CarritoProducto carritoProducto = carritoProductoRepository
            .findByCarritoIdAndProductoId(carrito.getId(), idProducto);

        if (carritoProducto != null) {
            int nuevaCantidad = carritoProducto.getCantidad() + cantidad;
            if (nuevaCantidad > producto.getStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay suficiente stock disponible");
            }
            carritoProducto.setCantidad(nuevaCantidad);
        } else {
            if (cantidad <= producto.getStock()) {
                carritoProducto = new CarritoProducto();
                CarritoProductoId id = new CarritoProductoId(carrito.getId(), idProducto);
                carritoProducto.setId(id);
                carritoProducto.setCarrito(carrito);
                carritoProducto.setProducto(producto);
                carritoProducto.setCantidad(cantidad);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay suficiente stock disponible");
            }
        }

        carrito.getCarritoProductos().add(carritoProducto);
        carrito.setCantidad(carrito.getCantidad() + cantidad);
        carritoRepository.save(carrito);
    }

    @Transactional
    public void modificarCantidadEnCarrito(Integer idUsuario, Integer idProducto, Integer cantidad) {
        Carrito carrito = carritoRepository.findByUsuarioId(idUsuario);
        if (carrito == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }

        CarritoProducto carritoProducto = carritoProductoRepository
            .findByCarritoIdAndProductoId(carrito.getId(), idProducto);

        if (carritoProducto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado en el carrito");
        }

        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (cantidad > producto.getStock()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay suficiente stock disponible");
        }

        // Actualizar la cantidad total del carrito
        int diferencia = cantidad - carritoProducto.getCantidad();
        carrito.setCantidad(carrito.getCantidad() + diferencia);
        
        carritoProducto.setCantidad(cantidad);
        carritoRepository.save(carrito);
    }

    @Transactional(readOnly = true)
    public Carrito obtenerCarrito(Integer idUsuario) {
        Carrito carrito = carritoRepository.findByUsuarioId(idUsuario);
        if (carrito == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }
        
        // Forzar la carga de los productos del carrito
        carrito.getCarritoProductos().size();
        return carrito;
    }

    @Transactional
    public void vaciarCarrito(Integer idUsuario) {
        Carrito carrito = carritoRepository.findByUsuarioId(idUsuario);
        if (carrito == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Carrito no encontrado");
        }

        carrito.getCarritoProductos().clear();
        carrito.setCantidad(0);
        carritoRepository.save(carrito);
    }
}