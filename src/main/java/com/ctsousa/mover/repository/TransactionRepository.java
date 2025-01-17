package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.TransactionEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("""
            SELECT t
            FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END ASC
            """)
    Page<TransactionEntity> findByPeriod(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, Pageable pageable);

    @Query("""
            SELECT t
            FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
            AND ABS(t.value) = :value
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END ASC
            """)
    Page<TransactionEntity> findByPeriodAndValue(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("value") BigDecimal value, Pageable pageable);

    @Query("""
            SELECT t FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category c
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND (sb.description LIKE %:description% OR c.description LIKE %:description% OR t.description LIKE %:description%)
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END ASC
            """)
    Page<TransactionEntity> findByPeriodAndDescription(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("description") String description, Pageable pageable);

    @Query("""
            SELECT t FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category c
            JOIN FETCH t.account acc
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND t.account.id IN (:accounts)
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END ASC
            """)
    Page<TransactionEntity> findByPeriodAndAccount(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("accounts") List<Long> accounts, Pageable pageable);

    @Query("""
            SELECT t FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category c
            JOIN FETCH t.account acc
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND (sb.description LIKE %:description% OR c.description LIKE %:description% OR t.description LIKE %:description%)
              AND t.account.id IN (:accounts)
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END ASC
            """)
    Page<TransactionEntity> findByPeriodAndAccountAndDescription(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("accounts") List<Long> accounts, @Param("description") String description, Pageable pageable);

    @Query("""
            SELECT t FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category c
            JOIN FETCH t.account acc
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND ABS(t.value) = :value
              AND t.account.id IN (:accounts)
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END ASC
            """)
    Page<TransactionEntity> findByPeriodAndAccountAndValue(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("accounts") List<Long> accounts, @Param("value") BigDecimal value, Pageable pageable);

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
            	SELECT
            	    c.name AS NAME,
            	    COALESCE(c.initial_balance, 0) + COALESCE((
            	        SELECT SUM(t.value)
            	        FROM tb_transaction t
            	        WHERE t.account_id = c.id
            	          AND t.card_id IS NULL
            	          AND t.paid
            	    ), 0) AS BALANCE
            	FROM
            	    tb_account c
            	WHERE
            	    c.id IN (:accounts)
            ) AS TEMP
            """, nativeQuery = true)
    BigDecimal accountBalance(@Param("accounts") List<Long> accounts);

    @Deprecated
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

    @Deprecated
    @Query(value = "SELECT SUM(t.value * -1) AS DESPESA FROM tb_transaction t WHERE t.category_type = 'EXPENSE'", nativeQuery = true)
    BigDecimal expenseBalance();

    @Deprecated
    @Query(value = "SELECT SUM(t.value) AS RECEITA FROM tb_transaction t WHERE t.category_type = 'INCOME'", nativeQuery = true)
    BigDecimal incomeBalance();

    @Query("SELECT t.signature FROM TransactionEntity t WHERE t.id = :id")
    String findBySignature(@Param("id") Long id);
}
