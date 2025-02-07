package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsUserEntityByEmail(String email);

    boolean existsUserEntityByLogin(String login);

    @Query("SELECT u FROM UserEntity u WHERE u.clientId = :id AND u.password = :password")
    List<UserEntity> findByClientIdAndPassword(@Param("id") Long id, @Param("password") String password);

    Optional<UserEntity> findByLogin(String login);

    @NonNull
    @Override
    @Query("SELECT u FROM UserEntity u INNER JOIN FETCH u.profiles WHERE u.id = :id")
    Optional<UserEntity> findById(@NonNull @Param("id") Long id);
}