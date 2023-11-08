package com.ar.techforb.controllers;

import com.ar.techforb.db.DB_User;
import com.ar.techforb.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserControllers {

    private final UserServices userServices;

    @Autowired
    public UserControllers(UserServices userServices){
        this.userServices = userServices;
    }


    @Operation(summary = "Obtiene todos los usuarios en la base de datos")
    @GetMapping(path = "/get_all_users")
    public List<DB_User> getUsers(){
        return userServices.getUsers();
    }


    @Operation(summary = "Crear un nuevo usuario y lo guarda en la base de datos")
    @PostMapping(path = "/create_user")
    public ResponseEntity<Object> create_user( String nombre,
                                               String apellido,
                                               String password,
                                               Integer dni,
                                              @RequestParam(required = false) Integer celular){
        if (!userServices.cadenaDeTextoValida(nombre) || !userServices.cadenaDeTextoValida(apellido)){
            return new ResponseEntity<Object>("Nombre y Apellido no deben contener numeros ni carateres especiales",
                    HttpStatus.BAD_REQUEST);
        }
        DB_User user = new DB_User(nombre, apellido, password, dni, celular);
        return this.userServices.create_user(user);
    }

    @Operation(summary = "Inicio de sesion del usuario")
    @PutMapping(path = "/iniciar_sesion")
    public ResponseEntity<Object> iniciar_sesion(Integer dni, String password){
        if(userServices.userExist(dni)){
            return this.userServices.iniciarSesion(dni, password);
        } else {
            return new ResponseEntity<>("Usuario No Existe",HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Finalizacion de sesion del usuario")
    @PutMapping(path = "/cerrar_sesion")
    public ResponseEntity<Object> cerrar_sesion(Integer dni){
        if(userServices.userExist(dni)){
            return this.userServices.cerrarSesion(dni);
        } else {
            return new ResponseEntity<>("Usuario No Existe",HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Eliminacion de usuario en la base de datos")
    @DeleteMapping(path = "/eliminar_usuario")
    public ResponseEntity<Object> eliminar_usuario(Integer dni, String password){
        if(userServices.userExist(dni)){
            return this.userServices.eliminarUsuario(dni, password);
        } else {
            return new ResponseEntity<>("Usuario no existe", HttpStatus.BAD_REQUEST);
        }
    }
}
