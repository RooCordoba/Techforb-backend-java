package com.ar.techforb.repository;

import com.ar.techforb.db.DB_Tarjeta;
import com.ar.techforb.db.DB_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<DB_Tarjeta, Long> {

    Optional<DB_Tarjeta> findTarjetaByCbu(Long cbu);

    List<DB_Tarjeta> findTarjetasByUser_id(Long user_id);

    Optional<DB_Tarjeta> findTarjetaById(Long id);
}
