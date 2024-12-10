package com.ecommerce.ecommerce_backend.services;

import com.ecommerce.ecommerce_backend.models.Usuario;
import com.ecommerce.ecommerce_backend.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;  // Cambiar esta importación
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;  // Cambiar el tipo


    public UsuarioService(UsuarioRepository usuarioRepository, 
                         PasswordEncoder passwordEncoder) {  // Inyección por constructor
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registro(Usuario usuario) {
        // Validaciones previas
        if (usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico()) != null) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Encriptar la contraseña antes de guardar
        usuario.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
        return usuarioRepository.save(usuario);
    }

    public Usuario editarPerfil(Integer idUsuario, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    
        if (usuario.getNombre() != null) {
            usuarioExistente.setNombre(usuario.getNombre());
        }
    
        if (usuario.getApellidos() != null) {
            usuarioExistente.setApellidos(usuario.getApellidos());
        }
    
        if (usuario.getDireccion() != null) {
            usuarioExistente.setDireccion(usuario.getDireccion());
        }
    
        if (usuario.getContrasenya() != null && !usuario.getContrasenya().isEmpty()) {
            usuarioExistente.setContrasenya(passwordEncoder.encode(usuario.getContrasenya()));
        }
    
        if (usuario.getTelefonosUsuarios() != null) {
            usuarioExistente.getTelefonosUsuarios().clear();
            usuario.getTelefonosUsuarios().forEach(telefono -> {
                telefono.setUsuario(usuarioExistente);
                usuarioExistente.getTelefonosUsuarios().add(telefono);
            });
        }
    
        Usuario actualizado = usuarioRepository.save(usuarioExistente);
        actualizado.setContrasenya(null); // Limpiar contraseña antes de devolver
        return actualizado;
    }}