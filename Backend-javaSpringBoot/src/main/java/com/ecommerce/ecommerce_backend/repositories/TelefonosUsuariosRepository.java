package com.ecommerce.ecommerce_backend.repositories;

import com.ecommerce.ecommerce_backend.models.TelefonosUsuarios;
import com.ecommerce.ecommerce_backend.models.TelefonosUsuariosId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TelefonosUsuariosRepository extends JpaRepository<TelefonosUsuarios, TelefonosUsuariosId> {

    @Modifying
@Query("DELETE FROM TelefonosUsuarios tu WHERE tu.usuario.id = :userId")
void deleteByUsuarioId(@Param("userId") Integer userId);
    
}