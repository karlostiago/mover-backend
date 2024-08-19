package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.BrandEntity;
import lombok.NonNull;
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

    @NonNull
    @Override
    @Query("SELECT b FROM BrandEntity b WHERE 1 = 1 ORDER BY b.name ASC")
    List<BrandEntity> findAll();

    List<BrandEntity> findByNameContainingIgnoreCase(@Param("name") String name);
}
