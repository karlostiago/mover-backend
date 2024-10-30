package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.entity.ModelEntity;
import com.ctsousa.mover.enumeration.CardType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM CardEntity c WHERE UPPER(c.name) = :name AND c.account.id = :accountId AND c.cardType = :cardType AND c.id NOT IN (:id)")
    boolean existsByNameAndAccountIdAndIconNotId(@Param("name") String name, @Param("accountId") Long accountId, @Param("cardType") CardType cardType, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM CardEntity c WHERE UPPER(c.name) = :name AND c.account.id = :accountId AND c.cardType = :cardType")
    boolean existsByNameAndAccountIdAndIcon(@Param("name") String name, @Param("accountId") Long accountId, @Param("cardType") CardType cardType);

    @Query("SELECT c FROM CardEntity c INNER JOIN FETCH c.account ac WHERE ac.number LIKE %:query% OR UPPER(c.name) LIKE %:query%")
    List<CardEntity> findBy(@Param("query") String query);

    @NonNull
    @Override
    @Query("SELECT c FROM CardEntity c INNER JOIN FETCH c.account WHERE c.id = :id")
    Optional<CardEntity> findById(@NonNull @Param("id") Long id);

    @NonNull
    @Override
    @Query("SELECT c FROM CardEntity c INNER JOIN FETCH c.account WHERE 1 = 1")
    List<CardEntity> findAll();
}
