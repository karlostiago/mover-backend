package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {

    @NonNull
    @Override
    @Query("""
            SELECT f FROM FineEntity f
            JOIN FETCH f.client
            JOIN FETCH f.vehicle
            JOIN FETCH f.account
            LEFT JOIN FETCH f.card
            """)
    List<FineEntity> findAll();
}
