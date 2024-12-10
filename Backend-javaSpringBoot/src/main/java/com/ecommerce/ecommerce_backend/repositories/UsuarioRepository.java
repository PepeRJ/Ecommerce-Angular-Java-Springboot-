package com.ecommerce.ecommerce_backend.repositories;

import com.ecommerce.ecommerce_backend.models.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.telefonosUsuarios tu LEFT JOIN FETCH tu.telefonos WHERE u.id = :id")
    Optional<Usuario> findByIdWithTelefonos(@Param("id") Integer id);
}