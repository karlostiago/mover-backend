package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.entity.UserEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserEntity u SET u.name = :name, u.email = :email, u.active = :active WHERE u.id = :id ")
    void updateNameAndEmail(@Param("id") Long id, @Param("name") String name, @Param("email") String email, @Param("active") Boolean active);

    @Modifying
    @Transactional
    @Query(value = "UPDATE UserEntity u SET u.password = :password WHERE u.id = :id ")
    void updatePassword(@Param("id") Long id, @Param("password") String password);

    @NonNull
    @Override
    @Query("SELECT u FROM UserEntity u INNER JOIN FETCH u.profiles  WHERE 1 = 1")
    List<UserEntity> findAll();
}