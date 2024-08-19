package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.SymbolEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SymbolRepository extends JpaRepository<SymbolEntity, Long> {

    boolean existsByDescription(@Param("description") String description);

    SymbolEntity findByDescription(@Param("description") String description);

    @NonNull
    @Override
    @Query("SELECT s FROM SymbolEntity s WHERE s.id NOT IN (SELECT b.symbol.id FROM BrandEntity b) ORDER BY s.description ")
    List<SymbolEntity> findAll();
}
