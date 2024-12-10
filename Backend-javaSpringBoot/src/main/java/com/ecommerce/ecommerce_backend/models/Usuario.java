package com.ecommerce.ecommerce_backend.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellidos;
    
    @Column(name = "correo_electronico")
    @JsonProperty("correo_electronico")
    private String correoElectronico;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String contrasenya;
    
    private String direccion;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TelefonosUsuarios> telefonosUsuarios = new ArrayList<>();

    // Constructor vacío necesario para JPA
    public Usuario() {
    }

    // Constructor con campos
    public Usuario(String nombre, String apellidos, String correoElectronico, String contrasenya, String direccion) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.contrasenya = contrasenya;
        this.direccion = direccion;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<TelefonosUsuarios> getTelefonosUsuarios() {
        return telefonosUsuarios;
    }

    public void setTelefonosUsuarios(List<TelefonosUsuarios> telefonosUsuarios) {
        if (telefonosUsuarios != null) {
            this.telefonosUsuarios.clear();
            this.telefonosUsuarios.addAll(telefonosUsuarios);
            this.telefonosUsuarios.forEach(t -> t.setUsuario(this));
        }
    }

    public void addTelefono(Telefonos telefono) {
        TelefonosUsuarios telefonoUsuario = new TelefonosUsuarios();
        telefonoUsuario.setUsuario(this);
        telefonoUsuario.setTelefonos(telefono);
        TelefonosUsuariosId id = new TelefonosUsuariosId();
        id.setIdUsuario(this.getId());
        id.setIdTelefonos(telefono.getId());
        telefonoUsuario.setId(id);
        
        // Asegurarse de que la lista no sea null
        if (this.telefonosUsuarios == null) {
            this.telefonosUsuarios = new ArrayList<>();
        }
        
        // Eliminar cualquier relación existente con el mismo teléfono
        this.telefonosUsuarios.removeIf(tu -> 
            tu.getTelefonos().getNumero().equals(telefono.getNumero()));
        
        this.telefonosUsuarios.add(telefonoUsuario);
    }
    public void removeTelefono(Telefonos telefono) {
        this.telefonosUsuarios.removeIf(tu -> 
            tu.getTelefonos().getId().equals(telefono.getId()));
    }

    public void clearTelefonos() {
        this.telefonosUsuarios.clear();
    }

    public void updateTelefonos(List<TelefonosUsuarios> nuevosTelefonos) {
        this.telefonosUsuarios.clear();
        if (nuevosTelefonos != null) {
            this.telefonosUsuarios.addAll(nuevosTelefonos);
            this.telefonosUsuarios.forEach(t -> t.setUsuario(this));
        }
    }

    // Métodos equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id) &&
               Objects.equals(correoElectronico, usuario.correoElectronico);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, correoElectronico);
    }

    @Transient
private List<Map<String, String>> telefonos;

public List<Map<String, String>> getTelefonos() {
    return telefonos;
}

public void setTelefonos(List<Map<String, String>> telefonos) {
    this.telefonos = telefonos;
}

    // toString para depuración
    @Override
    public String toString() {
        return "Usuario{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", apellidos='" + apellidos + '\'' +
               ", correoElectronico='" + correoElectronico + '\'' +
               ", direccion='" + direccion + '\'' +
               ", telefonos=" + telefonosUsuarios.size() + // Añadido contador de teléfonos
               '}';
    }
}