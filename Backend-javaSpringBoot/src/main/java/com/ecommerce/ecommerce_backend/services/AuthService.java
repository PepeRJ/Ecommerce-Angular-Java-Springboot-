package com.ecommerce.ecommerce_backend.services;

import com.ecommerce.ecommerce_backend.models.TelefonosUsuarios;
import com.ecommerce.ecommerce_backend.models.TelefonosUsuariosId;
import com.ecommerce.ecommerce_backend.models.Telefonos;
import com.ecommerce.ecommerce_backend.models.Usuario;
import com.ecommerce.ecommerce_backend.repositories.UsuarioRepository;
import com.ecommerce.ecommerce_backend.repositories.TelefonosRepository;
import com.ecommerce.ecommerce_backend.repositories.TelefonosUsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TelefonosRepository telefonosRepository;

    @Autowired
    private TelefonosUsuariosRepository telefonosUsuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpSession session;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Map<String, Object> registro(Usuario usuario) {
        System.out.println("=== INICIO REGISTRO ===");
        System.out.println("Datos recibidos para registro:");
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Teléfonos recibidos: " + usuario.getTelefonos());

        if (usuario.getContrasenya() == null || usuario.getContrasenya().trim().isEmpty()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        
        Optional<Usuario> usuarioExistente = usuarioRepository.findByCorreoElectronico(
            usuario.getCorreoElectronico()
        );
        
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        
        if (usuario.getTelefonos() != null && !usuario.getTelefonos().isEmpty()) {
            for (Map<String, String> telefonoMap : usuario.getTelefonos()) {
                String numero = telefonoMap.get("numero");
                if (numero != null && !numero.trim().isEmpty()) {
                    Telefonos nuevoTelefono = new Telefonos();
                    nuevoTelefono.setNumero(numero);
                    nuevoTelefono = telefonosRepository.save(nuevoTelefono);

                    TelefonosUsuariosId id = new TelefonosUsuariosId();
                    id.setIdUsuario(nuevoUsuario.getId());
                    id.setIdTelefonos(nuevoTelefono.getId());

                    TelefonosUsuarios telefonoUsuario = new TelefonosUsuarios();
                    telefonoUsuario.setId(id);
                    telefonoUsuario.setUsuario(nuevoUsuario);
                    telefonoUsuario.setTelefonos(nuevoTelefono);
                    
                    telefonosUsuariosRepository.save(telefonoUsuario);
                }
            }
        }

        session.setAttribute("usuario_id", nuevoUsuario.getId());
        
        Map<String, Object> userData = obtenerDatosUsuario(nuevoUsuario.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Registro exitoso");
        response.put("usuario", userData);
        
        System.out.println("=== FIN REGISTRO ===");
        System.out.println("Datos finales: " + response);
        
        return response;
    }

    public Map<String, Object> inicioSesion(String email, String password) {
        try {
            System.out.println("=== INICIO LOGIN ===");
            System.out.println("Email: " + email);
            
            Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreoElectronico(email);
            if (!usuarioOpt.isPresent()) {
                throw new RuntimeException("Usuario no encontrado");
            }
    
            Usuario usuario = usuarioOpt.get();
            if (!passwordEncoder.matches(password, usuario.getContrasenya())) {
                throw new RuntimeException("Contraseña incorrecta");
            }
    
            session.setAttribute("usuario_id", usuario.getId());

            Map<String, Object> userData = obtenerDatosUsuario(usuario.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inicio de sesión exitoso");
            response.put("usuario", userData);
            
            System.out.println("=== FIN LOGIN ===");
            System.out.println("Datos de respuesta: " + response);
            
            return response;
        } catch (Exception e) {
            System.out.println("Error en inicioSesion: " + e.getMessage());
            throw e;
        }
    }

    public void cerrarSesion() {
        session.invalidate();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> obtenerDatosUsuario(Integer id_usuario) {
        try {
            System.out.println("=== INICIO OBTENER DATOS USUARIO ===");
            System.out.println("ID Usuario: " + id_usuario);

            Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", usuario.getId());
            userData.put("nombre", usuario.getNombre());
            userData.put("apellidos", usuario.getApellidos());
            userData.put("correo_electronico", usuario.getCorreoElectronico());
            userData.put("direccion", usuario.getDireccion());
            
            List<Map<String, String>> telefonos = new ArrayList<>();
            System.out.println("Cargando teléfonos para usuario: " + id_usuario);
            
            if (usuario.getTelefonosUsuarios() != null) {
                System.out.println("Cantidad de teléfonos encontrados: " + usuario.getTelefonosUsuarios().size());
                for (TelefonosUsuarios tu : usuario.getTelefonosUsuarios()) {
                    if (tu.getTelefonos() != null) {
                        Map<String, String> telefono = new HashMap<>();
                        String numero = tu.getTelefonos().getNumero();
                        System.out.println("Agregando teléfono: " + numero);
                        telefono.put("numero", numero);
                        telefonos.add(telefono);
                    }
                }
            }
            
            userData.put("telefonos", telefonos);
            System.out.println("Datos completos a enviar: " + userData);
            
            System.out.println("=== FIN OBTENER DATOS USUARIO ===");
            return userData;
        } catch (Exception e) {
            System.out.println("Error en obtenerDatosUsuario: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public Map<String, Object> editarPerfil(Integer id_usuario, Usuario usuarioNuevo, Integer usuarioAutenticadoId) {
        try {
            System.out.println("=== INICIO EDITAR PERFIL ===");
            System.out.println("Datos recibidos para actualización:");
            System.out.println("ID: " + id_usuario);
            System.out.println("Nombre: " + usuarioNuevo.getNombre());
            System.out.println("Dirección: " + usuarioNuevo.getDireccion());
            System.out.println("Teléfonos recibidos: " + usuarioNuevo.getTelefonos());

            if (!usuarioAutenticadoId.equals(id_usuario)) {
                throw new RuntimeException("No autorizado");
            }

            Usuario usuarioExistente = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Actualizar campos básicos
            if (usuarioNuevo.getNombre() != null) {
                usuarioExistente.setNombre(usuarioNuevo.getNombre());
            }
            if (usuarioNuevo.getApellidos() != null) {
                usuarioExistente.setApellidos(usuarioNuevo.getApellidos());
            }
            if (usuarioNuevo.getDireccion() != null) {
                usuarioExistente.setDireccion(usuarioNuevo.getDireccion());
            }
            if (usuarioNuevo.getContrasenya() != null && !usuarioNuevo.getContrasenya().isEmpty()) {
                usuarioExistente.setContrasenya(passwordEncoder.encode(usuarioNuevo.getContrasenya()));
            }

            // Actualizar teléfonos
            List<Map<String, String>> nuevosTelefonos = usuarioNuevo.getTelefonos();
            if (nuevosTelefonos != null && !nuevosTelefonos.isEmpty()) {
                System.out.println("Procesando nuevos teléfonos: " + nuevosTelefonos);
                
                // Eliminar teléfonos existentes
                telefonosUsuariosRepository.deleteByUsuarioId(id_usuario);
                entityManager.flush();

                for (Map<String, String> telefonoMap : nuevosTelefonos) {
                    String numero = telefonoMap.get("numero");
                    if (numero != null && !numero.trim().isEmpty()) {
                        // Crear y guardar teléfono
                        Telefonos nuevoTelefono = new Telefonos();
                        nuevoTelefono.setNumero(numero);
                        nuevoTelefono = telefonosRepository.save(nuevoTelefono);
                        entityManager.flush();

                        // Crear y guardar relación
                        TelefonosUsuariosId telefonoUsuarioId = new TelefonosUsuariosId();
                        telefonoUsuarioId.setIdUsuario(usuarioExistente.getId());
                        telefonoUsuarioId.setIdTelefonos(nuevoTelefono.getId());

                        TelefonosUsuarios telefonoUsuario = new TelefonosUsuarios();
                        telefonoUsuario.setId(telefonoUsuarioId);
                        telefonoUsuario.setUsuario(usuarioExistente);
                        telefonoUsuario.setTelefonos(nuevoTelefono);

                        telefonosUsuariosRepository.save(telefonoUsuario);
                        entityManager.flush();
                    }
                }
            }

            usuarioExistente = usuarioRepository.save(usuarioExistente);
            entityManager.flush();
            
            Map<String, Object> resultado = obtenerDatosUsuario(id_usuario);
            System.out.println("=== FIN EDITAR PERFIL ===");
            System.out.println("Datos finales: " + resultado);
            
            return resultado;
        } catch (Exception e) {
            System.out.println("Error en editarPerfil: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}