package com.ar.techforb.db;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Table
@Entity
public class DB_Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tarjeta_id")
    @Schema(description = "tarjeta de donde se hizo la transaccion",
            required = true,
            defaultValue = "tarjeta")
    private DB_Tarjeta tarjeta;
    @Schema(description = "monto de la transaccion",
            required = true,
            defaultValue = "monto")
    private Float monto;
    @Schema(description = "cbu de la tarjeta destina a la que se realiza la transaccion",
            required = true,
            defaultValue = "cbu_destino")
    private Long cbu_destino;

    public DB_Transaccion(){}

    public DB_Transaccion(DB_Tarjeta tarjeta, Float monto, Long cbu_destino) {
        this.tarjeta = tarjeta;
        this.monto = monto;
        this.cbu_destino = cbu_destino;
    }

    public Long getId() {
        return id;
    }

    public DB_Tarjeta getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(DB_Tarjeta tarjeta) {
        this.tarjeta = tarjeta;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public Long getCbu_destino() {
        return cbu_destino;
    }

    public void setCbu_destino(Long cbu_destino) {
        this.cbu_destino = cbu_destino;
    }
}
