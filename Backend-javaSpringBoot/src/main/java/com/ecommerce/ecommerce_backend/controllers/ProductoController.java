package com.ecommerce.ecommerce_backend.controllers;

import com.ecommerce.ecommerce_backend.models.Producto;
import com.ecommerce.ecommerce_backend.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/producto")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> getListaProductos() {
        List<Producto> productos = productoService.getListaProductos();
        return ResponseEntity.ok(productos); // El JSON con todas las imágenes se debería devolver aquí
    }

    @PostMapping
    public ResponseEntity<?> createProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.createProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.editProducto(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable Integer id) {
        productoService.deleteProducto(id);
        return ResponseEntity.ok("Producto eliminado con éxito");
    }
}