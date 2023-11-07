package com.ar.techforb.controllers;

import com.ar.techforb.db.DB_User;
import com.ar.techforb.models.DB_User_model;
import com.ar.techforb.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Stream;

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
    public ResponseEntity<Object> create_user(@RequestBody @Valid DB_User_model user_model,BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            // para que solo me muestre los mensajes del error sin detalles tecnicos
            Stream mensaje = bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage);

            return new ResponseEntity<Object>(mensaje, HttpStatus.BAD_REQUEST);
        }

        DB_User user = new DB_User(user_model.getNombre(),user_model.getApellido(),
                user_model.getPassword(), Integer.parseInt(user_model.getDni()) ,
                user_model.getCelular());

        return this.userServices.create_user(user);
    }
}
