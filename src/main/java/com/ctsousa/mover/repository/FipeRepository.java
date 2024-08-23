package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.FipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FipeRepository extends JpaRepository<FipeEntity, Long> {

    @Query("SELECT CASE WHEN COUNT(f.id) > 0 THEN TRUE ELSE FALSE END FROM FipeEntity f WHERE f.code = :code")
    boolean existsByCode(@Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(f.id) > 0 THEN TRUE ELSE FALSE END FROM FipeEntity f WHERE f.hash = :hash")
    boolean existsByHash(@Param("hash") String hash);

    FipeEntity findByHash(String hash);

    FipeEntity findByCode(String code);
}
