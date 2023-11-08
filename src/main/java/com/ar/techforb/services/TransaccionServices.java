package com.ar.techforb.services;

import com.ar.techforb.db.DB_Tarjeta;
import com.ar.techforb.db.DB_Transaccion;
import com.ar.techforb.db.DB_User;
import com.ar.techforb.repository.TarjetaRepository;
import com.ar.techforb.repository.TransaccionRepository;
import com.ar.techforb.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransaccionServices {

    private final TransaccionRepository transaccionRepository;
    private final TarjetaRepository tarjetaRepository;
    private final UsersRepository usersRepository;
    private final UserServices userServices;

    public TransaccionServices(TransaccionRepository transaccionRepository,
                               TarjetaRepository tarjetaRepository,
                               UsersRepository usersRepository, UserServices userServices) {
        this.transaccionRepository = transaccionRepository;
        this.tarjetaRepository = tarjetaRepository;
        this.usersRepository = usersRepository;
        this.userServices = userServices;
    }

    public ResponseEntity<Object> depositar(Integer dni,
                                            String password,
                                            Float monto_a_depositar,
                                            Long tarjeta_id){
        try {
            if (userServices.coincidenCredenciales(dni, password)){
                DB_User user = usersRepository.findUserByDni(dni).get();
                Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaById(tarjeta_id);
                if(!tarjeta.isPresent()){
                    return new ResponseEntity<>("La tarjeta no existe", HttpStatus.BAD_REQUEST);
                }

                if(tarjeta.get().getUser().equals(user)){
                    if(monto_a_depositar<=0){
                        return new ResponseEntity<>("El monto a depositar tiene que ser mayor a 0",
                                HttpStatus.BAD_REQUEST);
                    }
                    tarjeta.get().setSaldo(tarjeta.get().getSaldo()+ monto_a_depositar);
                    tarjetaRepository.save(tarjeta.get());
                    DB_Transaccion transaccion = new DB_Transaccion(tarjeta.get(),
                                                                    monto_a_depositar,
                                                                    tarjeta.get().getCbu());
                    transaccionRepository.save(transaccion);
                    return new ResponseEntity<>("Monto depositado con exito, tu saldo es de: " +
                            tarjeta.get().getSaldo(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Tarjeta no pertenece al user", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Object>("Contraseña Incorrecta", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> extraer(Integer dni,
                                          String password,
                                          Float monto_a_extraer,
                                          Long tarjeta_id) {
        try {
            if (userServices.coincidenCredenciales(dni, password)) {
                DB_User user = usersRepository.findUserByDni(dni).get();
                Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaById(tarjeta_id);
                if (!tarjeta.isPresent()) {
                    return new ResponseEntity<>("La tarjeta no existe", HttpStatus.BAD_REQUEST);
                }
                if (tarjeta.get().getUser().equals(user)) {
                    if (monto_a_extraer <= 0) {
                        return new ResponseEntity<>("El monto a extraer tiene que ser mayor a 0",
                                HttpStatus.BAD_REQUEST);
                    }
                    if (monto_a_extraer > tarjeta.get().getSaldo()) {
                        return new ResponseEntity<>("No hay dinero suficiente para extraer, el total del saldo es: "
                                + tarjeta.get().getSaldo(),
                                HttpStatus.BAD_REQUEST);
                    }
                    tarjeta.get().setSaldo(tarjeta.get().getSaldo() - monto_a_extraer);
                    tarjetaRepository.save(tarjeta.get());
                    DB_Transaccion transaccion = new DB_Transaccion(tarjeta.get(),
                            monto_a_extraer,
                            tarjeta.get().getCbu());
                    transaccionRepository.save(transaccion);
                    return new ResponseEntity<>("Monto extraido con exito, tu saldo es de: " +
                            tarjeta.get().getSaldo(), HttpStatus.OK);

                } else {
                    return new ResponseEntity<>("Tarjeta no pertenece al user", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Object>("Contraseña Incorrecta", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> transferir(Integer dni,
                                             String password,
                                             Float monto_a_transferir,
                                             Long tarjeta_id,
                                             Long cbu_destino) {
        try {
            if (userServices.coincidenCredenciales(dni, password)) {
                DB_User user = usersRepository.findUserByDni(dni).get();
                Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaById(tarjeta_id);
                if (!tarjeta.isPresent()) {
                    return new ResponseEntity<>("La tarjeta no existe", HttpStatus.BAD_REQUEST);
                }
                if (tarjeta.get().getUser().equals(user)) {
                    if (monto_a_transferir <= 0) {
                        return new ResponseEntity<>("El monto a transferir tiene que ser mayor a 0",
                                HttpStatus.BAD_REQUEST);
                    }
                    if (monto_a_transferir > tarjeta.get().getSaldo()) {
                        return new ResponseEntity<>("No hay dinero suficiente para transferir, el total del saldo es: "
                                + tarjeta.get().getSaldo(),
                                HttpStatus.BAD_REQUEST);
                    }
                    Optional<DB_Tarjeta> tarjeta_destino = tarjetaRepository.findTarjetaByCbu(cbu_destino);
                    if(Objects.equals(cbu_destino, tarjeta.get().getCbu())){
                        return new ResponseEntity<>("El cbu destino tiene que ser para otra tarjeta, seleccione "+
                                " la opcion de depositar si quiere agregar dinero a la cuenta.",
                                HttpStatus.BAD_REQUEST);
                    }
                    if (!tarjeta_destino.isPresent()){
                        return new ResponseEntity<>("La tarjeta a la que intenta transferir no existe",
                                HttpStatus.BAD_REQUEST);
                    }
                    tarjeta.get().setSaldo(tarjeta.get().getSaldo() - monto_a_transferir);
                    tarjetaRepository.save(tarjeta.get());
                    tarjeta_destino.get().setSaldo(tarjeta_destino.get().getSaldo() + monto_a_transferir);
                    tarjetaRepository.save(tarjeta_destino.get());
                    DB_Transaccion transaccion = new DB_Transaccion(tarjeta.get(),
                            -monto_a_transferir,
                            tarjeta.get().getCbu());
                    DB_Transaccion transaccion_destino = new DB_Transaccion(tarjeta_destino.get(),
                            monto_a_transferir,
                            tarjeta_destino.get().getCbu());
                    transaccionRepository.save(transaccion);
                    transaccionRepository.save(transaccion_destino);
                    return new ResponseEntity<>("Monto transferido con exito, tu saldo es de: " +
                            tarjeta.get().getSaldo(), HttpStatus.OK);

                } else {
                    return new ResponseEntity<>("Tarjeta no pertenece al user", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<Object>("Contraseña Incorrecta", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public ResponseEntity<Object> ver_historial_movimientos(Integer dni,
                                                            Long tarjeta_id){
        try{
            DB_User user = usersRepository.findUserByDni(dni).get();
            Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaById(tarjeta_id);

            if(!tarjeta.isPresent() || !tarjeta.get().getUser().equals(user)){
                return new ResponseEntity<>("Tarjeta seleccionada no existe en la base de datos del usuario",
                        HttpStatus.BAD_REQUEST);
            }
            List<DB_Transaccion> transacciones = transaccionRepository.findTransaccionByTarjeta_id(tarjeta_id);
            return new ResponseEntity<>(transacciones,HttpStatus.OK);
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
