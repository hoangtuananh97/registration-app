package com.example.registrationapp.persistence.dao;

import com.example.registrationapp.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    @Query(
            value = "SELECT * FROM USER WHERE USERNAME = :username AND ID != :id",
            nativeQuery = true
    )
    User findByUsernameAndNotId(String username, long id);
}
