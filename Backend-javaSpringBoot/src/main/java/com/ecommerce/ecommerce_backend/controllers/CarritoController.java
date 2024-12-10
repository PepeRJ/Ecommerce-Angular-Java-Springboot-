package com.ecommerce.ecommerce_backend.controllers;

import com.ecommerce.ecommerce_backend.models.Carrito;
import com.ecommerce.ecommerce_backend.services.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carrito")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PostMapping("/add")
    public ResponseEntity<?> addProductoAlCarrito(@RequestBody Map<String, Integer> requestBody) {
        Integer idUsuario = requestBody.get("id_usuario");
        Integer idProducto = requestBody.get("id_producto");
        Integer cantidad = requestBody.get("cantidad");

        System.out.println("Agregando producto al carrito. Usuario ID: " + idUsuario + 
                          ", Producto ID: " + idProducto + 
                          ", Cantidad: " + cantidad);

        try {
            carritoService.agregarProductoAlCarrito(idUsuario, idProducto, cantidad);
            return ResponseEntity.ok(Map.of("message", "Producto añadido al carrito con éxito"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al agregar producto al carrito"));
        }
    }

    @PutMapping("/modificar-cantidad/{id_usuario}")
    public ResponseEntity<?> modificarCantidadEnCarrito(
            @PathVariable Integer id_usuario,
            @RequestBody Map<String, Integer> requestBody) {
        
        Integer idProducto = requestBody.get("id_producto");
        Integer cantidad = requestBody.get("cantidad");

        System.out.println("Modificando cantidad en el carrito. Usuario ID: " + id_usuario + 
                          ", Producto ID: " + idProducto + 
                          ", Nueva Cantidad: " + cantidad);

        try {
            carritoService.modificarCantidadEnCarrito(id_usuario, idProducto, cantidad);
            return ResponseEntity.ok(Map.of("message", "Cantidad modificada con éxito"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al modificar la cantidad"));
        }
    }

    @GetMapping("/{id_usuario}")
    public ResponseEntity<?> getCarrito(@PathVariable("id_usuario") Integer idUsuario) {
        System.out.println("Obteniendo carrito para el usuario ID: " + idUsuario);

        try {
            Carrito carrito = carritoService.obtenerCarrito(idUsuario);
            Map<String, Object> response = new HashMap<>();
            response.put("carritoId", carrito.getId());

            if (carrito.getCarritoProductos().isEmpty()) {
                response.put("carritoProductos", new ArrayList<>());
                response.put("precioTotal", 0);
                return ResponseEntity.ok(response);
            }

            response.put("carritoProductos", carrito.getCarritoProductos().stream()
                .map(cp -> Map.of(
                    "producto", Map.of(
                        "id", cp.getProducto().getId(),
                        "nombre", cp.getProducto().getNombre(),
                        "precio", cp.getProducto().getPrecio()
                    ),
                    "cantidad", cp.getCantidad()
                ))
                .collect(Collectors.toList()));
            response.put("precioTotal", carrito.getPrecioTotal());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener el carrito"));
        }
    }

    @DeleteMapping("/vaciar/{id_usuario}")
    public ResponseEntity<?> vaciarCarrito(@PathVariable("id_usuario") Integer idUsuario) {
        System.out.println("Vaciando carrito para el usuario ID: " + idUsuario);

        try {
            carritoService.vaciarCarrito(idUsuario);
            return ResponseEntity.ok(Map.of("message", "Carrito vaciado con éxito"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al vaciar el carrito"));
        }
    }
}