package com.ecommerce.ecommerce_backend.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TelefonosUsuariosId implements Serializable {
    
    private Integer idUsuario;
    private Integer idTelefonos;

    // Constructor por defecto
    public TelefonosUsuariosId() {}

    public TelefonosUsuariosId(Integer idUsuario, Integer idTelefonos) {
        this.idUsuario = idUsuario;
        this.idTelefonos = idTelefonos;
    }

    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdTelefonos() {
        return idTelefonos;
    }

    public void setIdTelefonos(Integer idTelefonos) {
        this.idTelefonos = idTelefonos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelefonosUsuariosId that = (TelefonosUsuariosId) o;
        return Objects.equals(idUsuario, that.idUsuario) &&
               Objects.equals(idTelefonos, that.idTelefonos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idTelefonos);
    }
}