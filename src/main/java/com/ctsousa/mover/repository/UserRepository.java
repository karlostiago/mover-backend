package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.email = :email")
    List<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.login = :login")
    List<UserEntity> findByLogin(String login);

    @Query("SELECT u FROM UserEntity u WHERE u.clientId = :clientId AND u.password = :password")
    List<UserEntity> findByClientIdAndPassword(@Param("clientId") Long clientId, @Param("password") String password);

}
