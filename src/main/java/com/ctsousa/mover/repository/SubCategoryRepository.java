package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.SubCategoryEntity;
import com.ctsousa.mover.core.entity.SymbolEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM SubCategoryEntity c WHERE c.description = :description AND c.id NOT IN (:id)")
    boolean existsByDescriptionNotId(@Param("description") String description, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM SubCategoryEntity c WHERE c.description = :description")
    boolean existsByDescription(@Param("description") String description);

    @Query("SELECT c FROM SubCategoryEntity c INNER JOIN FETCH c.category ct WHERE c.description LIKE %:query% OR ct.description LIKE %:query%")
    List<SubCategoryEntity> findBy(@Param("query") String query);

    @NonNull
    @Override
    @Query("SELECT c FROM SubCategoryEntity c INNER JOIN FETCH c.category WHERE c.id = :id")
    Optional<SubCategoryEntity> findById(@NonNull @Param("id") Long id);

    @NonNull
    @Override
    @Query("SELECT c FROM SubCategoryEntity c INNER JOIN FETCH c.category WHERE 1 = 1")
    List<SubCategoryEntity> findAll();
}
