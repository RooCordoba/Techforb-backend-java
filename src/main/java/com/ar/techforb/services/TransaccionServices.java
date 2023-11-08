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

    private DB_User getUserByDniAndPassword(Integer dni, String password) {
        if (userServices.coincidenCredenciales(dni, password)) {
            return usersRepository.findUserByDni(dni).orElse(null);
        }
        return null;
    }

    private ResponseEntity<Object> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private DB_Transaccion createTransaccion(DB_Tarjeta tarjeta, Float monto) {
        return new DB_Transaccion(tarjeta, monto, tarjeta.getCbu());
    }

    public ResponseEntity<Object> depositar(Integer dni,
                                            String password,
                                            Float monto_a_depositar,
                                            Long tarjeta_id){
        try {
            DB_User user = getUserByDniAndPassword(dni, password);
            if (user == null) {
                return handleException(new Exception("Contraseña Incorrecta"));
            }
            Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaById(tarjeta_id);
            if (!tarjeta.isPresent() || !tarjeta.get().getUser().equals(user)) {
                return handleException(new Exception("La tarjeta no existe o no pertenece al usuario"));
            }
            if (monto_a_depositar <= 0) {
                return handleException(new Exception("El monto a depositar tiene que ser mayor a 0"));
            }
            tarjeta.get().setSaldo(tarjeta.get().getSaldo() + monto_a_depositar);
            tarjetaRepository.save(tarjeta.get());
            DB_Transaccion transaccion = createTransaccion(tarjeta.get(), monto_a_depositar);
            transaccionRepository.save(transaccion);
            return new ResponseEntity<>("Monto depositado con éxito, tu saldo es de: " + tarjeta.get().getSaldo(), HttpStatus.OK);
        } catch (Exception e){
            return handleException(e);
        }
    }

    public ResponseEntity<Object> extraer(Integer dni,
                                          String password,
                                          Float monto_a_extraer,
                                          Long tarjeta_id) {
        try {
            DB_User user = getUserByDniAndPassword(dni, password);
            if (user == null) {
                return handleException(new Exception("Contraseña Incorrecta"));
            }
            Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaById(tarjeta_id);
            if (!tarjeta.isPresent() || !tarjeta.get().getUser().equals(user)) {
                return handleException(new Exception("La tarjeta no existe o no pertenece al usuario"));
            }
            if (monto_a_extraer <= 0) {
                return handleException(new Exception("El monto a extraer tiene que ser mayor a 0"));
            }
            if (monto_a_extraer > tarjeta.get().getSaldo()) {
                return handleException(new Exception("No hay dinero suficiente para extraer, el total del saldo es: " + tarjeta.get().getSaldo()));
            }
            tarjeta.get().setSaldo(tarjeta.get().getSaldo() - monto_a_extraer);
            tarjetaRepository.save(tarjeta.get());
            DB_Transaccion transaccion = createTransaccion(tarjeta.get(), -monto_a_extraer);
            transaccionRepository.save(transaccion);
            return new ResponseEntity<>("Monto extraído con éxito, tu saldo es de: " + tarjeta.get().getSaldo(), HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
        }
    }

    public ResponseEntity<Object> transferir(Integer dni,
                                             String password,
                                             Float monto_a_transferir,
                                             Long tarjeta_id,
                                             Long cbu_destino) {
        try {
            DB_User user = getUserByDniAndPassword(dni, password);
            if (user == null) {
                return handleException(new Exception("Contraseña Incorrecta"));
            }
            Optional<DB_Tarjeta> tarjeta = tarjetaRepository.findTarjetaById(tarjeta_id);
            if (!tarjeta.isPresent() || !tarjeta.get().getUser().equals(user)) {
                return handleException(new Exception("La tarjeta no existe o no pertenece al usuario"));
            }
            if (monto_a_transferir <= 0) {
                return handleException(new Exception("El monto a transferir tiene que ser mayor a 0"));
            }
            if (monto_a_transferir > tarjeta.get().getSaldo()) {
                return handleException(new Exception("No hay dinero suficiente para transferir, el total del saldo es: " + tarjeta.get().getSaldo()));
            }
            if (Objects.equals(cbu_destino, tarjeta.get().getCbu())) {
                return handleException(new Exception("El cbu destino tiene que ser para otra tarjeta"));
            }
            Optional<DB_Tarjeta> tarjeta_destino = tarjetaRepository.findTarjetaByCbu(cbu_destino);

            if (!tarjeta_destino.isPresent()) {
                return handleException(new Exception("La tarjeta a la que intenta transferir no existe"));
            }
            tarjeta.get().setSaldo(tarjeta.get().getSaldo() - monto_a_transferir);
            tarjetaRepository.save(tarjeta.get());
            tarjeta_destino.get().setSaldo(tarjeta_destino.get().getSaldo() + monto_a_transferir);
            tarjetaRepository.save(tarjeta_destino.get());

            DB_Transaccion transaccion = createTransaccion(tarjeta.get(), -monto_a_transferir);
            DB_Transaccion transaccion_destino = createTransaccion(tarjeta_destino.get(), monto_a_transferir);
            transaccionRepository.save(transaccion);
            transaccionRepository.save(transaccion_destino);

            return new ResponseEntity<>("Monto transferido con éxito, tu saldo es de: " + tarjeta.get().getSaldo(), HttpStatus.OK);
        } catch (Exception e) {
            return handleException(e);
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
            return handleException(e);
        }
    }
}
