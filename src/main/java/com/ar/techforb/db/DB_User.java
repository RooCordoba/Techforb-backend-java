package com.ar.techforb.db;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table
public class DB_User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Nombre del usuario",
            required = true,
            defaultValue = "nombre")
    private String nombre;
    @Schema(description = "Apellido del usuario",
            required = true,
            defaultValue = "apellido")
    private String apellido;
    @Schema(description = "Password del usuario",
            required = true,
            defaultValue = "password")
    private String password;

    @Column(unique = true)
    @Schema(description = "Dni del usuario",
            required = true,
            defaultValue = "dni")
    private Integer dni;
    @Schema(description = "Celular del usuario",
            defaultValue = "celular")
    private Integer celular;
    private Boolean is_logged_in;

    public DB_User(){}
    public DB_User(String nombre, String apellido, String password, Integer dni, Integer celular) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.dni = dni;
        this.celular = celular;
        this.is_logged_in = false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
    }

    public Integer getCelular() {
        return celular;
    }

    public void setCelular(Integer celular) {
        this.celular = celular;
    }

    public Boolean getIs_logged_in() {
        return is_logged_in;
    }

    public void setIs_logged_in(Boolean is_logged_in) {
        this.is_logged_in = is_logged_in;
    }


}
