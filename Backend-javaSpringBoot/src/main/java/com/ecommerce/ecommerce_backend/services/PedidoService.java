package com.ecommerce.ecommerce_backend.services;

import com.ecommerce.ecommerce_backend.models.*;
import com.ecommerce.ecommerce_backend.repositories.*;
import com.ecommerce.ecommerce_backend.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoProductoRepository carritoProductoRepository;

    @Transactional
    public Pedido crearPedido(Integer idUsuario, Integer idCarrito) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito no encontrado"));

        List<CarritoProducto> productosEnCarrito = carritoProductoRepository
                .findByCarritoId(carrito.getId());

        if (productosEnCarrito.isEmpty()) {
            throw new BadRequestException("El carrito está vacío");
        }

        BigDecimal precioTotal = BigDecimal.ZERO;

        for (CarritoProducto carritoProducto : productosEnCarrito) {
            Producto producto = carritoProducto.getProducto();
            int cantidadVendida = carritoProducto.getCantidad();

            precioTotal = precioTotal.add(
                producto.getPrecio().multiply(BigDecimal.valueOf(cantidadVendida))
            );

            if (producto.getStock() < cantidadVendida) {
                throw new BadRequestException("No hay suficiente stock disponible para el producto " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - cantidadVendida);
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setCarrito(carrito);
        pedido.setFecha(LocalDateTime.now());
        pedido.setEstado("en camino");
        pedido.setPrecioTotal(precioTotal);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Vaciar el carrito después de crear el pedido
        carritoProductoRepository.deleteByCarritoId(carrito.getId());
        carrito.setCantidad(0);
        carritoRepository.save(carrito);

        return pedidoGuardado;
    }

    @Transactional
    public Pedido actualizarPedido(Integer idPedido, String estado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));

        pedido.setEstado(estado);
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void eliminarPedido(Integer idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado"));
        pedidoRepository.delete(pedido);
    }

    public List<Map<String, Object>> obtenerPedidosUsuario(Integer idUsuario) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioId(idUsuario);
        
        return pedidos.stream().map(pedido -> {
            List<CarritoProducto> productosCarrito = carritoProductoRepository
                .findByCarritoId(pedido.getCarrito().getId());

            List<Map<String, Object>> productos = productosCarrito.stream()
                .map(cp -> {
                    Map<String, Object> productoMap = new HashMap<>();
                    productoMap.put("nombre", cp.getProducto().getNombre());
                    productoMap.put("cantidad", cp.getCantidad());
                    productoMap.put("precio", cp.getProducto().getPrecio() + "€");
                    return productoMap;
                })
                .collect(Collectors.toList());

            Map<String, Object> pedidoMap = new HashMap<>();
            pedidoMap.put("id", pedido.getId());
            pedidoMap.put("fecha", pedido.getFecha());
            pedidoMap.put("estado", pedido.getEstado());
            pedidoMap.put("precio_total", pedido.getPrecioTotal());
            pedidoMap.put("productos", productos);
            return pedidoMap;
        }).collect(Collectors.toList());
    }
}