package com.ecommerce.ecommerce_backend.controllers;

import com.ecommerce.ecommerce_backend.models.Pedido;
import com.ecommerce.ecommerce_backend.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/pedido")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Map<String, Integer> requestBody) {
        try {
            Integer idUsuario = requestBody.get("id_usuario");
            Integer idCarrito = requestBody.get("id_carrito");
            
            Pedido pedido = pedidoService.crearPedido(idUsuario, idCarrito);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "pedido", pedido
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al crear el pedido"));
        }
    }

    @GetMapping("/{id_usuario}")
public ResponseEntity<?> obtenerPedidosUsuario(@PathVariable("id_usuario") Integer idUsuario) {
    try {
        List<Map<String, Object>> pedidos = pedidoService.obtenerPedidosUsuario(idUsuario);
        return ResponseEntity.ok(pedidos);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Error al obtener los pedidos del usuario"));
    }
}

    @PutMapping("/{id_pedido}")
    public ResponseEntity<?> actualizarPedido(
            @PathVariable("id_pedido") Integer idPedido,
            @RequestBody Map<String, String> requestBody) {
        try {
            String estado = requestBody.get("estado");
            pedidoService.actualizarPedido(idPedido, estado);
            return ResponseEntity.ok(Map.of("message", "Estado del pedido actualizado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar el pedido"));
        }
    }

    @DeleteMapping("/{id_pedido}")
    public ResponseEntity<?> eliminarPedido(@PathVariable("id_pedido") Integer idPedido) {
        try {
            pedidoService.eliminarPedido(idPedido);
            return ResponseEntity.ok(Map.of("message", "Pedido eliminado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al eliminar el pedido"));
        }
    }
}
