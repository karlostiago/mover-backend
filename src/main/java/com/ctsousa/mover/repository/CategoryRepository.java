package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.CategoryEntity;
import com.ctsousa.mover.enumeration.TypeCategory;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM CategoryEntity c WHERE c.description = :description AND c.type = :type AND c.id NOT IN (:id)")
    boolean existsByDescriptionNotId(@Param("description") String description, @Param("type") TypeCategory type, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM CategoryEntity c WHERE c.description = :description AND c.type = :type")
    boolean existsByDescriptionAndType(@Param("description") String description, @Param("type") TypeCategory type);

    @Query(value = "SELECT DISTINCT c.* FROM tb_category c LEFT JOIN tb_subcategory sb ON sb.category_id = c.id WHERE CASE " +
            "WHEN c.type = 'EXPENSE' THEN 'DESPESA' " +
            "WHEN c.type = 'INCOME' THEN 'RECEITA' " +
            "WHEN c.type = 'INVESTMENT' THEN 'INVESTIMENTO' " +
            "WHEN c.type = 'TRANSFER' THEN 'TRANSFERÊNCIA' " +
            "ELSE c.type END LIKE %:query% OR c.description LIKE %:query% OR sb.description LIKE %:query% " +
            "ORDER BY c.type ASC, c.description ASC", nativeQuery = true)
    List<CategoryEntity> findBy(@Param("query") String query);

    @Query("SELECT c FROM CategoryEntity c WHERE c.type = :type")
    List<CategoryEntity> findByType(@Param("type") TypeCategory type);

    @NonNull
    @Override
    @Query(value = "SELECT c.* FROM tb_category c WHERE 1 = 1 ORDER BY " +
        " CASE " +
            "WHEN c.type = 'EXPENSE' THEN 'DESPESA' " +
            "WHEN c.type = 'INCOME' THEN 'RECEITA' " +
            "WHEN c.type = 'INVESTMENT' THEN 'INVESTIMENTO' " +
            "WHEN c.type = 'TRANSFER' THEN 'TRANSFERÊNCIA' " +
            "ELSE c.type END ASC, " +
            "c.description ASC ", nativeQuery = true)
    List<CategoryEntity> findAll();

    @NonNull
    @Override
    @Query("SELECT c FROM CategoryEntity c LEFT JOIN FETCH c.subcategories WHERE c.id = :id")
    Optional<CategoryEntity> findById(@NonNull @Param("id") Long id);
}
