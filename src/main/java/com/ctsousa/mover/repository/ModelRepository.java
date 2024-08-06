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

    @Query("SELECT CASE WHEN COUNT(m.id) > 0 THEN TRUE ELSE FALSE END FROM ModelEntity m INNER JOIN m.brand b WHERE m.name = :modelName AND b.name = :brandName AND m.yearManufacture = :yearManufacture AND m.yearModel = :yearModel AND m.color = :color")
    boolean existsByName(@Param("modelName") String modelName, @Param("yearManufacture") Integer yearManufacture,
                         @Param("yearModel") Integer yearModel, @Param("color") String color, @Param("brandName") String brandName);

    @Query("SELECT CASE WHEN COUNT(m.id) > 0 THEN TRUE ELSE FALSE END FROM ModelEntity m INNER JOIN m.brand b WHERE m.name = :modelName AND b.name = :brandName AND m.yearManufacture = :yearManufacture AND m.yearModel = :yearModel AND m.color = :color AND m.id NOT IN(:id)")
    boolean existsByNameNotId(@Param("modelName") String modelName, @Param("yearManufacture") Integer yearManufacture,
                         @Param("yearModel") Integer yearModel, @Param("color") String color, @Param("brandName") String brandName, @Param("id") Long id);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE (m.name = :modelName OR b.name = :brandName OR m.color = :color)")
    List<ModelEntity> findBy(@Param("modelName") String modelName, @Param("color") String color, @Param("brandName") String brandName);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE m.color = :color AND m.yearManufacture = :yearManufacture")
    List<ModelEntity> findByColorAndYearManufacture(@Param("yearManufacture") Integer yearManufacture, @Param("color") String color);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE m.color = :color AND m.yearModel = :yearModel")
    List<ModelEntity> findByColorAndYearModel(@Param("yearModel") Integer yearModel, @Param("color") String color);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE b.name = :brandName AND m.yearManufacture = :yearManufacture")
    List<ModelEntity> findByBrandNameAndYearManufacture(@Param("yearManufacture") Integer yearManufacture, @Param("brandName") String brandName);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE b.name = :brandName AND m.yearModel = :yearModel")
    List<ModelEntity> findByBrandNameAndYearModel(@Param("yearModel") Integer yearModel, @Param("brandName") String brandName);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE m.name = :modelName AND m.yearManufacture = :yearManufacture")
    List<ModelEntity> findByModelNameAndYearManufacture(@Param("yearManufacture") Integer yearManufacture, @Param("modelName") String modelName);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE m.name = :modelName AND m.yearModel = :yearModel")
    List<ModelEntity> findByModelNameAndYearModel(@Param("yearModel") Integer yearModel, @Param("modelName") String modelName);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE m.yearModel = :yearModel")
    List<ModelEntity> findByYearModel(@Param("yearModel") Integer yearModel);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE m.yearManufacture = :yearManufacture")
    List<ModelEntity> findByYearManufacture(@Param("yearManufacture") Integer yearManufacture);

    @Query("SELECT m FROM ModelEntity m INNER JOIN FETCH m.brand b WHERE m.yearModel = :yearModel AND m.yearManufacture = :yearManufacture")
    List<ModelEntity> findByYearManufactureAndYearModel(@Param("yearManufacture") Integer yearManufacture, @Param("yearModel") Integer yearModel);
}
