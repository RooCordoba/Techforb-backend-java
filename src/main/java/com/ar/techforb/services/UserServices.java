package com.ar.techforb.services;

import com.ar.techforb.db.DB_User;
import com.ar.techforb.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class UserServices {
    private final UsersRepository usersRepository;

    @Autowired
    public UserServices(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    public Boolean userExist(Integer dni){
        if ( usersRepository.findUserByDni(dni).isPresent()){
            return true;
        }
        return false;
    }
    public List<DB_User> getUsers(){
        try{
            return this.usersRepository.findAll();
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> create_user(DB_User user){
        try{
            Optional<DB_User> res = usersRepository.findUserByDni(user.getDni());
            if(res.isPresent()) {
                throw new RuntimeException("Usuario con ese DNI ya existe");
            } else {
                usersRepository.save(user);
                return new ResponseEntity<>("Usuario creado con Exito: "+ user.getNombre(),HttpStatus.CREATED);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Boolean cadenaDeTextoValida(String cadena){
        String specialChars = "~`!@#$%^&*()-_=+\\|[{]};:'\",<.>/?";
        for (int i = 0; i < cadena.length(); i++) {
            if (specialChars.contains(String.valueOf(cadena.charAt(i))) ||
                    Character.isDigit(Integer.valueOf(cadena.charAt(i)))) {
                return false;
            }
        }
        return true;
    }

    public Boolean coincidenCredenciales(Integer dni, String password){
        Optional<DB_User> user = usersRepository.findUserByDni(dni);
        return user.get().getPassword().equals(password);
    }

    public ResponseEntity<Object> iniciarSesion(Integer dni, String password){
        try{
            Optional<DB_User> user = usersRepository.findUserByDni(dni);
            if(coincidenCredenciales(dni, password)){
                user.get().setIs_logged_in(true);
                usersRepository.save(user.get());
                return new ResponseEntity<>("Inicio de sesion correcto!",HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Contraseña Incorrecta",HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> cerrarSesion(Integer dni){
        try{
            Optional<DB_User> user = usersRepository.findUserByDni(dni);
            user.get().setIs_logged_in(false);
            usersRepository.save(user.get());
            return new ResponseEntity<>("Sesion finalizada correctamente!",HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> eliminarUsuario(Integer dni, String password){
        try{
            Optional<DB_User> user = usersRepository.findUserByDni(dni);
            if(user.get().getPassword().equals(password)){
                usersRepository.delete(user.get());
                return new ResponseEntity<>("Usuario eliminado",HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Contraseña Incorrecta",HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
