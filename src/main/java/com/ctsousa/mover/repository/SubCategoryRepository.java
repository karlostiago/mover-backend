package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.core.entity.SubCategoryEntity;
import com.ctsousa.mover.enumeration.TypeCategory;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM SubCategoryEntity c WHERE c.description = :description AND c.category = :category AND c.id NOT IN (:id)")
    boolean existsByDescriptionNotId(@Param("description") String description,  @Param("category") CategoryEntity category, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM SubCategoryEntity c WHERE c.description = :description AND c.category = :category")
    boolean existsByDescription(@Param("description") String description, @Param("category") CategoryEntity category);

    @Query("SELECT c FROM SubCategoryEntity c INNER JOIN FETCH c.category ct WHERE c.description LIKE %:query% OR ct.description LIKE %:query% ORDER BY ct.type ASC, ct.description ASC, c.description ASC ")
    List<SubCategoryEntity> findBy(@Param("query") String query);

    @Query("SELECT c FROM SubCategoryEntity c INNER JOIN FETCH c.category ct WHERE ct.type = :type ORDER BY ct.type ASC, ct.description ASC, c.description ASC ")
    List<SubCategoryEntity> findBy(@Param("type") TypeCategory type);

    @NonNull
    @Override
    @Query("SELECT c FROM SubCategoryEntity c INNER JOIN FETCH c.category WHERE c.id = :id")
    Optional<SubCategoryEntity> findById(@NonNull @Param("id") Long id);

    @NonNull
    @Override
    @Query("SELECT c FROM SubCategoryEntity c INNER JOIN FETCH c.category ct WHERE 1 = 1 ORDER BY ct.type ASC, ct.description ASC, c.description ASC ")
    List<SubCategoryEntity> findAll();
}
