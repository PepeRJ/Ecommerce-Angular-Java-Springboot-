package com.ecommerce.ecommerce_backend.controllers;

import com.ecommerce.ecommerce_backend.models.Usuario;
import com.ecommerce.ecommerce_backend.models.TelefonosUsuarios;
import com.ecommerce.ecommerce_backend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/autenticacion")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        try {
            // Log de datos básicos
            System.out.println("Datos recibidos en registro:");
            System.out.println("Nombre: " + usuario.getNombre());
            System.out.println("Apellidos: " + usuario.getApellidos());
            System.out.println("Email: " + usuario.getCorreoElectronico());
            System.out.println("Dirección: " + usuario.getDireccion());
            System.out.println("Contraseña presente: " + (usuario.getContrasenya() != null));
            
            // Log específico para teléfonos
            if (usuario.getTelefonosUsuarios() != null) {
                System.out.println("Teléfonos a registrar: " + usuario.getTelefonosUsuarios().size());
                usuario.getTelefonosUsuarios().forEach(telefonoUsuario -> 
                    System.out.println("Teléfono: " + telefonoUsuario.getTelefonos().getNumero()));
            } else {
                System.out.println("No se recibieron teléfonos para registrar");
            }
            
            Map<String, Object> response = authService.registro(usuario);
            System.out.println("Registro exitoso. Respuesta: " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error en registro: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Error en el registro", e.getMessage()));
        }
    }

    @PostMapping("/inicio-sesion")
    public ResponseEntity<?> inicioSesion(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("correo_electronico");
            String password = credentials.get("contrasenya");

            if (email == null || password == null) {
                throw new RuntimeException("Email y contraseña son requeridos");
            }

            System.out.println("Intento de inicio de sesión para: " + email);
            Map<String, Object> loginResult = authService.inicioSesion(email, password);
            System.out.println("Inicio de sesión exitoso para: " + email);
            return ResponseEntity.ok(loginResult);
        } catch (Exception e) {
            System.out.println("Error en inicio de sesión: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Error en el inicio de sesión", e.getMessage()));
        }
    }

    @PostMapping("/cerrar-sesion")
    public ResponseEntity<?> cerrarSesion() {
        try {
            authService.cerrarSesion();
            return ResponseEntity.ok()
                .body(Map.of("message", "Sesión cerrada exitosamente"));
        } catch (Exception e) {
            System.out.println("Error al cerrar sesión: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Error al cerrar sesión", e.getMessage()));
        }
    }

    @GetMapping("/perfil/{id_usuario}")
    public ResponseEntity<?> obtenerDatosUsuario(@PathVariable Integer id_usuario, HttpSession session) {
        try {
            Integer usuarioId = (Integer) session.getAttribute("usuario_id");
            if (usuarioId == null || !usuarioId.equals(id_usuario)) {
                throw new RuntimeException("No autorizado");
            }

            Map<String, Object> userData = authService.obtenerDatosUsuario(id_usuario);
            System.out.println("Obteniendo datos del usuario: " + id_usuario);
            
            // Log de teléfonos encontrados
            Object telefonos = userData.get("telefonos");
            if (telefonos != null) {
                System.out.println("Teléfonos encontrados: " + telefonos);
            }

            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            System.out.println("Error al obtener datos: " + e.getMessage());
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Error al obtener datos", e.getMessage()));
        }
    }

    @PutMapping("/perfil/{id_usuario}")
    public ResponseEntity<?> editarPerfil(
            @PathVariable Integer id_usuario, 
            @RequestBody Usuario usuario, 
            HttpSession session) {
        try {
            Integer usuarioId = (Integer) session.getAttribute("usuario_id");
            
            System.out.println("Actualizando perfil para usuario: " + id_usuario);
            System.out.println("ID en sesión: " + usuarioId);
            
            // Log detallado de teléfonos a actualizar
            if (usuario.getTelefonosUsuarios() != null) {
                System.out.println("Teléfonos a actualizar: " + usuario.getTelefonosUsuarios().size());
                usuario.getTelefonosUsuarios().forEach(telefonoUsuario -> {
                    if (telefonoUsuario.getTelefonos() != null) {
                        System.out.println("Teléfono: " + telefonoUsuario.getTelefonos().getNumero());
                    }
                });
            } else {
                System.out.println("No se recibieron teléfonos para actualizar");
            }

            Map<String, Object> userData = authService.editarPerfil(id_usuario, usuario, usuarioId);
            System.out.println("Perfil actualizado exitosamente");
            return ResponseEntity.ok(userData);
        } catch (Exception e) {
            System.out.println("Error en editarPerfil: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Error al editar perfil", e.getMessage()));
        }
    }
}

class ErrorResponse {
    private String error;
    private String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
}