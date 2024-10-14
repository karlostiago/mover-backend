package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(p.id) > 0 THEN TRUE ELSE FALSE END FROM PartnerEntity p WHERE (p.email = :email) AND p.id NOT IN (:id)")
    boolean existsByEmailNotId(@Param("email") String email, @Param("id") Long id);

    @Query("SELECT p FROM PartnerEntity p WHERE p.name LIKE %:query% OR p.email LIKE %:query%")
    List<PartnerEntity> findBy(@Param("query") String query);
}
