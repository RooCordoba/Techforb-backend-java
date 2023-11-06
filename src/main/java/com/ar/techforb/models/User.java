package com.ar.techforb.models;


public class User {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String password;
    private Integer dni;
    private Integer celular;
    private Boolean is_logged_in;

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

    public User(String nombre, String apellido, String password, Integer dni, Integer celular, Boolean is_logged_in) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.dni = dni;
        this.celular = celular;
        this.is_logged_in = is_logged_in;
    }
}
