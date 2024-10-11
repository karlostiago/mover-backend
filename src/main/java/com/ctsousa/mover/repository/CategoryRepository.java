package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM CategoryEntity c WHERE c.description = :description AND c.id NOT IN (:id)")
    boolean existsByDescriptionNotId(@Param("description") String description, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM CategoryEntity c WHERE c.description = :description")
    boolean existsByDescription(@Param("description") String description);

    @Query("SELECT c FROM CategoryEntity c WHERE c.description LIKE %:query%")
    List<CategoryEntity> findBy(@Param("query") String query);
}
