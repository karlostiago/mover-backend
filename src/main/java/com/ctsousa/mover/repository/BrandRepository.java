package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Long> {

    boolean existsByName(@Param("name") String name);

    List<BrandEntity> findByNameContainingIgnoreCase(@Param("name") String name);
}
