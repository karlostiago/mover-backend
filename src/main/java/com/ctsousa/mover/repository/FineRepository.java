package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.FineProjection;
import com.ctsousa.mover.core.entity.FineEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {

    @Query(value = """
            SELECT
            	f.id,
            	f.description,
            	f.value,
            	f.due_date,
            	f.date_time_of_commitment,
            	COALESCE(t.paid, TRUE) AS paid
            FROM tb_fine f
            LEFT JOIN tb_transaction t ON t.signature = f.signature
            WHERE 1 = 1
            """, nativeQuery = true)
    List<FineProjection> findAllWithProjection();
}
