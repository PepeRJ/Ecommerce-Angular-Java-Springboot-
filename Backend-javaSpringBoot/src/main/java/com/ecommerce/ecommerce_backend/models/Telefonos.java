package com.ecommerce.ecommerce_backend.models;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "telefonos")
public class Telefonos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String numero;

    @OneToMany(mappedBy = "telefonos")
private List<TelefonosUsuarios> telefonosUsuarios;

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}