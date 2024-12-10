package com.ecommerce.ecommerce_backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "telefonos_usuarios")
public class TelefonosUsuarios {

    @EmbeddedId
    private TelefonosUsuariosId id;

    @ManyToOne
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @MapsId("idTelefonos")
    @JoinColumn(name = "id_telefonos")
    private Telefonos telefonos;

    // Getters y Setters
    public TelefonosUsuariosId getId() {
        return id;
    }

    public void setId(TelefonosUsuariosId id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Telefonos getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(Telefonos telefonos) {
        this.telefonos = telefonos;
    }
    
}
