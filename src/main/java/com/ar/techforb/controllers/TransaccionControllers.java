package com.ar.techforb.controllers;

import com.ar.techforb.repository.TarjetaRepository;
import com.ar.techforb.services.TransaccionServices;
import com.ar.techforb.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/transacciones")
public class TransaccionControllers {

    private final TransaccionServices transaccionServices;
    private final UserServices userServices;
    private final TarjetaRepository tarjetaRepository;

    @Autowired
    public TransaccionControllers(TransaccionServices transaccionServices, UserServices userServices, TarjetaRepository tarjetaRepository){
        this.transaccionServices = transaccionServices;
        this.userServices = userServices;
        this.tarjetaRepository = tarjetaRepository;
    }


    @Operation(summary = "deposita dinero en una cuenta del usuario")
    @PostMapping(path = "/depositar_dinero")
    public ResponseEntity<Object> depositar_dinero(Integer dni,
                                                   String password,
                                                   Float monto_a_depositar,
                                                   Long tarjeta_id){
        if (userServices.userExist(dni)){
            return transaccionServices.depositar(dni, password, monto_a_depositar, tarjeta_id);
        } else {
            return new ResponseEntity<>("Usuario no existe", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "extrae dinero en una cuenta del usuario")
    @PostMapping(path = "/extraer_dinero")
    public ResponseEntity<Object> extraer_dinero(Integer dni,
                                                   String password,
                                                   Float monto_a_extraer,
                                                   Long tarjeta_id,
                                                   Long cbu_destino){
        if (userServices.userExist(dni)){
            return transaccionServices.extraer(dni, password, monto_a_extraer, tarjeta_id);
        } else {
            return new ResponseEntity<>("Usuario no existe", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "transferir dinero en una cuenta a otra")
    @PostMapping(path = "/transferir_dinero")
    public ResponseEntity<Object> transferir_dinero(Integer dni,
                                                 String password,
                                                 Float monto_a_trasferir,
                                                 Long tarjeta_id,
                                                 Long cbu_destino){
        if (userServices.userExist(dni)){
            return transaccionServices.transferir(dni, password, monto_a_trasferir, tarjeta_id, cbu_destino);
        } else {
            return new ResponseEntity<>("Usuario no existe", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "obtener movimientos de una tarjeta del user")
    @GetMapping(path = "/movimientos")
    public ResponseEntity<Object> movimientos(Integer dni,
                                                    Long tarjeta_id){
        if (userServices.userExist(dni)){
            return transaccionServices.ver_historial_movimientos(dni, tarjeta_id);
        } else {
            return new ResponseEntity<>("Usuario no existe", HttpStatus.BAD_REQUEST);
        }
    }

}
