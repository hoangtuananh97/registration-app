package com.example.registrationapp.persistence.dao;

import com.example.registrationapp.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    @Query(
            value = "SELECT * FROM USER WHERE USERNAME = :username AND ID != :id",
            nativeQuery = true
    )
    User findByUsernameAndNotId(String username, long id);
    @Query(
            value = "SELECT * FROM USER WHERE IS_ENABLED IS TRUE",
            nativeQuery = true
    )
    List<User> findUsersByEnabledIsTrue();
    @Query(
            value = "SELECT * FROM USER WHERE IS_ENABLED IS TRUE AND ID != :id",
            nativeQuery = true
    )
    User findUserByEnabledIsTrueAndId(long id);
}
