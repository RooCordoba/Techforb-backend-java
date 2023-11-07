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

    public List<DB_User> getUsers(){
        return this.usersRepository.findAll();
    }

    public ResponseEntity<Object> create_user(DB_User user){
        Optional<DB_User> res = usersRepository.findUserByDni(user.getDni());
        HttpStatus estado = HttpStatus.CREATED;
        if(res.isPresent()){
            estado = HttpStatus.CONFLICT;
            return new ResponseEntity<Object>("Usuario con ese DNI ya existe",estado);
        }
        else {
            usersRepository.save(user);
            return new ResponseEntity<>("Usuario creado con Exito: "+ user.getNombre(),estado);
        }
    }
}
