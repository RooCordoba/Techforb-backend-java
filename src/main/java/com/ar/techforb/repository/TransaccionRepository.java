package com.ar.techforb.repository;

import aj.org.objectweb.asm.Opcodes;
import com.ar.techforb.db.DB_Tarjeta;
import com.ar.techforb.db.DB_Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransaccionRepository extends JpaRepository<DB_Transaccion, Long> {

    List<DB_Transaccion> findTransaccionByTarjeta_id(Long id);

}

