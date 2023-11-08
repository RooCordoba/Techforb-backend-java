package com.ar.techforb.services;

import com.ar.techforb.db.DB_Tarjeta;
import com.ar.techforb.db.DB_User;
import com.ar.techforb.repository.TarjetaRepository;
import com.ar.techforb.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.round;

@Service
public class TarjetaServices {
    private final TarjetaRepository tarjetaRepository;

    private final UserServices userServices;
    private final UsersRepository usersRepository;

    //@Autowired
//    public void UserControllers(UserServices userServices){
//        this.userServices = userServices;
//    }

    @Autowired
    public TarjetaServices(TarjetaRepository tarjetaRepository,
                           UserServices userServices,
                           UsersRepository usersRepository){
        this.tarjetaRepository = tarjetaRepository;
        this.userServices = userServices;
        this.usersRepository = usersRepository;
    }

    public Long generateCBU(){
        try{
            boolean cbu_no_disponible = true;
            long cbu = 0L;
            while (cbu_no_disponible){
                cbu = round(1000000000 + Math.random() * 900000000);
                Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaByCbu(cbu);
                if(!tarjeta.isPresent()){
                    cbu_no_disponible = false;
                }
            }
            return cbu;
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    public ResponseEntity<Object> crear_tarjeta(Integer dni, String password){
        try{
            DB_User user = usersRepository.findUserByDni(dni).get();
            if(userServices.coincidenCredenciales(dni, password)){
                DB_Tarjeta tarjeta = new DB_Tarjeta(user, generateCBU());
                tarjetaRepository.save(tarjeta);
                return  new ResponseEntity<>("Tarjeta creada para el usario " + user.getNombre()+
                        " con dni "+ user.getDni(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Contraseña Incorrecta",HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> ver_datos_tarjeta(Integer dni, String password){
        try{
            DB_User user = usersRepository.findUserByDni(dni).get();
            if(userServices.coincidenCredenciales(dni, password)){
                List<DB_Tarjeta> tarjetas = tarjetaRepository.findTarjetasByUser_id(user.getId());
                return new ResponseEntity<>(tarjetas,HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Contraseña Incorrecta",HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
        throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> eliminar_tarjeta(Integer dni,String password,Long id_de_tarjeta){
        try{
            DB_User user = usersRepository.findUserByDni(dni).get();
            if(userServices.coincidenCredenciales(dni, password)){
                Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaById(id_de_tarjeta);
                if (!tarjeta.isPresent()){
                    return new ResponseEntity<>("Tarjeta no existe",HttpStatus.BAD_REQUEST);
                }
                if (tarjeta.get().getUser().equals(user)){
                    tarjetaRepository.delete(tarjeta.get());
                    return new ResponseEntity<>("Tarjeta eliminada con éxito", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Tarjeta no pertenece al usuario", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Contraseña Incorrecta",HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
