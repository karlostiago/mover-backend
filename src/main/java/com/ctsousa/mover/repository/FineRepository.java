package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.FineProjection;
import com.ctsousa.mover.core.entity.FineEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FineRepository extends JpaRepository<FineEntity, Long> {

    @Query(value = """
            SELECT
            	f.id,
            	f.description,
            	f.value,
            	f.due_date,
            	f.date_time_of_commitment,
            	COALESCE(t.paid, FALSE) AS paid,
            	v.license_plate,
             	m.name AS model,
             	b.name AS brand,
             	t.id AS transaction_id
            FROM tb_fine f
            JOIN tb_vehicle v ON v.id = f.vehicle_id
            JOIN tb_model m ON m.id = v.model_id
            JOIN tb_brand b ON b.id = m.brand_id
            LEFT JOIN tb_transaction t ON t.signature = f.signature
            WHERE 1 = 1
            """, nativeQuery = true)
    List<FineProjection> findAllWithProjection();

    @NonNull
    @Override
    @Query("""
            SELECT f FROM FineEntity f
            JOIN FETCH f.client
            JOIN FETCH f.vehicle v
            JOIN FETCH v.model
            JOIN FETCH v.brand
            JOIN FETCH f.account
            LEFT JOIN FETCH f.card
            WHERE f.id = :id
            """)
    Optional<FineEntity> findById(@NonNull Long id);
}
