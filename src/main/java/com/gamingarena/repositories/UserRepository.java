package com.gamingarena.repositories;

import com.gamingarena.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users, Long> {

    Users findByEmail(String email);

    Users findByEmailAndPassword(String email, String password);

    Users findByUsername(String username);

    Users findByIdAndPassword(long id, String password);

}
