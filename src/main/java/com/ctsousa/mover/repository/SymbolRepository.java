package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.SymbolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SymbolRepository extends JpaRepository<SymbolEntity, Long> {

    boolean existsByDescription(@Param("description") String description);

    SymbolEntity findByDescription(@Param("description") String description);
}
