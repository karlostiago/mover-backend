package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {

    boolean existsByName(@Param("name") String name);

    @Query("SELECT CASE WHEN COUNT(b.id) > 0 THEN TRUE ELSE FALSE END FROM BrandEntity b WHERE b.name = :name AND b.id NOT IN (:id)")
    boolean existsByNameNotId(@Param("name") String name, @Param("id") Long id);

    List<BrandEntity> findByNameContainingIgnoreCase(@Param("name") String name);
}
