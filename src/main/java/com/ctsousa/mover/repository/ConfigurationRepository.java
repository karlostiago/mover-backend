package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.ConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM ConfigurationEntity c WHERE c.key = :key AND c.id NOT IN (:id)")
    boolean existsByKeyNotId(@Param("key") String key, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(c.id) > 0 THEN TRUE ELSE FALSE END FROM ConfigurationEntity c WHERE c.key = :key")
    boolean existsByKey(@Param("key") String key);

    @Query("SELECT c FROM ConfigurationEntity c WHERE c.key LIKE %:query% OR c.value LIKE %:query%")
    List<ConfigurationEntity> findBy(@Param("query") String query);
}
