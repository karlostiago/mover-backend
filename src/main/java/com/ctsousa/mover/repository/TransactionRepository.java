package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t " +
            "JOIN FETCH t.subcategory sb " +
            "JOIN FETCH sb.category " +
            "JOIN FETCH t.account " +
            "LEFT JOIN FETCH t.card " +
            "LEFT JOIN FETCH t.vehicle " +
            "LEFT JOIN FETCH t.contract " +
            "LEFT JOIN FETCH t.partner " +
            "WHERE t.signature = :signature ORDER BY t.id DESC ")
    List<TransactionEntity> findBySignature(@Param("signature") String signature);

    @NonNull
    @Override
    @Query("SELECT t FROM TransactionEntity t " +
            "JOIN FETCH t.subcategory sb " +
            "JOIN FETCH sb.category " +
            "JOIN FETCH t.account " +
            "LEFT JOIN FETCH t.card " +
            "LEFT JOIN FETCH t.vehicle " +
            "LEFT JOIN FETCH t.contract " +
            "LEFT JOIN FETCH t.partner " +
            "WHERE 1 = 1 ORDER BY t.id DESC ")
    List<TransactionEntity> findAll();

    @NonNull
    @Override
    @Query("SELECT t FROM TransactionEntity t " +
            "JOIN FETCH t.subcategory sb " +
            "JOIN FETCH sb.category " +
            "JOIN FETCH t.account " +
            "LEFT JOIN FETCH t.card " +
            "LEFT JOIN FETCH t.vehicle " +
            "LEFT JOIN FETCH t.contract " +
            "LEFT JOIN FETCH t.partner " +
            "WHERE t.id = :id ORDER BY t.id DESC ")
    Optional<TransactionEntity> findById(@NonNull Long id);

    @Query(value = """
            SELECT
            	SUM(TEMP.BALANCE) AS BALANCE
            FROM (
            	SELECT\s
            	    c.name AS NAME,
            	    COALESCE(c.initial_balance, 0) + COALESCE((
            	        SELECT SUM(t.value)
            	        FROM tb_transaction t
            	        WHERE t.account_id = c.id
            	          AND t.card_id IS NULL
            	          AND t.paid
            	    ), 0) AS BALANCE
            	FROM\s
            	    tb_account c
            	WHERE\s
            	    c.id IN (:accounts)
            	    AND c.caution = :scrowAccount
            ) AS TEMP
            """, nativeQuery = true)
    BigDecimal balance(@Param("accounts") List<Long> accounts, @Param("scrowAccount") Boolean scrowAccount);

    @Query(value = """
            SELECT
            	SUM(TEMP.BALANCE) AS BALANCE
            FROM (
            	SELECT\s
            	    c.name AS NAME,
            	    COALESCE(c.limit, 0) + COALESCE((
            	        SELECT SUM(t.value)
            	        FROM tb_transaction t
            	        WHERE t.account_id = c.account_id
            	          AND t.card_id = c.id
            	          AND t.paid
            	    ), 0) AS BALANCE
            	FROM\s
            	    tb_card c
            	WHERE\s
            	    c.id IN (:cards)
            ) AS TEMP
            """, nativeQuery = true)
    BigDecimal creditBalance(@Param("cards") List<Long> cards);
}
