package com.ar.techforb.repository;

import com.ar.techforb.db.DB_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //me permite trabajar desde services con la base de datos
public interface UsersRepository extends JpaRepository<DB_User, Long> {

    Optional<DB_User> findUserByDni(Integer dni);
}
