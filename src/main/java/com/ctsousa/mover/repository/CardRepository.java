package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {

//    boolean existsByHash(String hash);
//
//    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM AccountEntity c WHERE c.number = :number AND c.name = :name AND c.id NOT IN (:id)")
//    boolean existsByNumberAndNameNotId(@Param("number") String number, @Param("name") String name, @Param("id") Long id);
//
    @Query("SELECT c FROM CardEntity c WHERE c.name LIKE %:query%")
    List<CardEntity> findBy(@Param("query") String query);
}
