package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsUserEntityByEmail(String email);

    boolean existsUserEntityByLogin(String login);

    @Query("SELECT u FROM UserEntity u WHERE u.id = :id AND u.password = :password")
    List<UserEntity> findByClientIdAndPassword(@Param("id") Long id, @Param("password") String password);

}
