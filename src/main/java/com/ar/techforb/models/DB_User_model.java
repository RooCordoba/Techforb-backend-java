//package com.ar.techforb.models;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.persistence.Column;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.Pattern;
//import jakarta.validation.constraints.Positive;
//
//
//public class DB_User_model{
//
//    @Schema(description = "Nombre del usuario",
//            required = true,
//            defaultValue = "nombre")
//    @NotBlank(message = "El nombre no tiene que ser vacío")
//    @Pattern(regexp = "^[a-zA-Z ]+$",
//            message = "El nombre debe contener solo letras y espacios")
//    private String nombre;
//
//    @Schema(description = "Apellido del usuario",
//            required = true,
//            defaultValue = "apellido")
//    @NotBlank(message = "El Apellido no tiene que ser vacío")
//    @Pattern(regexp = "^[a-zA-Z ]+$",
//            message = "El Apellido debe contener solo letras y espacios")
//    private String apellido;
//
//    @Schema(description = "Password del usuario",
//            required = true,
//            defaultValue = "password")
//    @NotBlank(message = "El password no tiene que ser vacío")
//    private String password;
//
//    @Column(unique = true)
//    @Schema(description = "DNI del usuario",
//            required = true,
//            defaultValue = "1234")
//    @Positive(message= "El dni tiene que ser un numero positivo")
//    @Pattern(regexp = "^[0-9]+$", message = "El DNI debe contener solo números")
//    private String dni;
//
//    @Schema(description = "Numero de celular del usuario",
//            defaultValue = "123456789")
//    @Positive(message= "El Celular tiene que ser un numero positivo")
//    private Integer celular;
//
//
//    public String getNombre() {
//        return nombre;
//    }
//
//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }
//
//    public String getApellido() {
//        return apellido;
//    }
//
//    public void setApellido(String apellido) {
//        this.apellido = apellido;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getDni() {
//        return dni;
//    }
//
//    public void setDni(String dni) {
//        this.dni = dni;
//    }
//
//    public Integer getCelular() {
//        return celular;
//    }
//
//    public void setCelular(Integer celular) {
//        this.celular = celular;
//    }
//}
