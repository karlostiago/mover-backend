package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ModelEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository<ModelEntity, Long> {

    @NonNull
    @Override
    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand")
    List<ModelEntity> findAll();

    @NonNull
    @Override
    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand WHERE m.id = :id")
    Optional<ModelEntity> findById(@NonNull @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(m.id) > 0 THEN TRUE ELSE FALSE END FROM ModelEntity m INNER JOIN m.brand b WHERE m.name = :modelName AND b.name = :brandName")
    boolean existsByNameAndBrandName(@Param("modelName") String modelName, @Param("brandName") String brandName);

    @Query("SELECT CASE WHEN COUNT(m.id) > 0 THEN TRUE ELSE FALSE END FROM ModelEntity m INNER JOIN m.brand b WHERE m.name = :modelName AND b.name = :brandName AND m.id NOT IN(:id)")
    boolean existsByNameAndBrandNameNotId(@Param("modelName") String modelName, @Param("brandName") String brandName, @Param("id") Long id);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE m.name LIKE %:name% OR b.name LIKE %:name%")
    List<ModelEntity> findBy(@Param("name") String name);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE b.id = :brandId")
    List<ModelEntity> findByBrandId(@Param("brandId") Long brandId);
}
