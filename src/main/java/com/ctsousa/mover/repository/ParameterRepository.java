package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ParameterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterRepository extends JpaRepository<ParameterEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM ParameterEntity c WHERE c.key = :key AND c.id NOT IN (:id)")
    boolean existsByKeyNotId(@Param("key") String key, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM ParameterEntity c WHERE c.key = :key")
    boolean existsByKey(@Param("key") String key);

    @Query("SELECT c FROM ParameterEntity c WHERE c.key LIKE %:query% OR c.value LIKE %:query%")
    List<ParameterEntity> findBy(@Param("query") String query);
}
