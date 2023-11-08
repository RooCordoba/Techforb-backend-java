package com.ar.techforb.db;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


@Table
@Entity
public class DB_Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(description = "Id del usuario de la tarjeta",
            required = true,
            defaultValue = "user_id")
    private DB_User user;
    @Column(unique = true)
    @Schema(description = "cbu de la tarjeta",
            required = true,
            defaultValue = "cbu")
    private Long cbu;
    @Schema(description = "Saldo disponible de la tarjeta",
            required = true,
            defaultValue = "saldo")
    private Float saldo;

    public DB_Tarjeta(){}

    public DB_Tarjeta(DB_User user, Long cbu) {
        this.user = user;
        this.cbu = cbu ;
        this.saldo = 0f;
    }

    public DB_User getUser() {
        return user;
    }

    public void setUser(DB_User user) {
        this.user = user;
    }

    public Long getCbu() {
        return cbu;
    }

    public void setCbu(Long cbu) {
        this.cbu = cbu;
    }

    public Float getSaldo() {
        return saldo;
    }

    public void setSaldo(Float saldo) {
        this.saldo = saldo;
    }
}
