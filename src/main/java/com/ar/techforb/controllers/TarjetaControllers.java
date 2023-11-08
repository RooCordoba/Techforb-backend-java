package com.ar.techforb.controllers;

import com.ar.techforb.services.TarjetaServices;
import com.ar.techforb.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/tarjetas")
public class TarjetaControllers {

    private final TarjetaServices tarjetaServices;
    private final UserServices userServices;

    @Autowired
    public TarjetaControllers(TarjetaServices tarjetaServices, UserServices userServices){this.tarjetaServices = tarjetaServices;
        this.userServices = userServices;
    }

    @Operation(summary = "Crea una tarjeta para el user")
    @PostMapping(path = "/pedir_tarjeta")
    public ResponseEntity<Object> pedir_tarjeta(Integer dni,
                                                String password
                                                ){
        if(userServices.userExist(dni)){
            return this.tarjetaServices.crear_tarjeta(dni, password);
        } else {
            return new ResponseEntity<>("Usuario No Existe",HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Muestra los datos de las tarjetas del user")
    @GetMapping(path = "/ver_datos_tarjeta")
    public ResponseEntity<Object> ver_tarjetas_en_posesion( Integer dni,
                                                            String password
                                                            ){
        if(userServices.userExist(dni)){
            return this.tarjetaServices.ver_datos_tarjeta(dni, password);
        } else {
            return new ResponseEntity<>("Usuario No Existe",HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Elimina una tarjeta del user")
    @GetMapping(path = "/eliminar_tarjeta")
    public ResponseEntity<Object> eliminar_tarjeta( Integer dni,
                                                    String password,
                                                    Long id_de_tarjeta
    ){
        if(userServices.userExist(dni)){
            return this.tarjetaServices.eliminar_tarjeta(dni, password, id_de_tarjeta);
        } else {
            return new ResponseEntity<>("Usuario No Existe",HttpStatus.BAD_REQUEST);
        }
    }

}
