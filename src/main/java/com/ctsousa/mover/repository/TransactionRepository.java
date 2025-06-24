package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.SubCategoryEntity;
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
            SELECT t.id
            FROM TransactionEntity t
            WHERE t.invoiceId IS NULL AND ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND t.value != 0
              AND NOT EXISTS (
                   SELECT DISTINCT ipd.invoice.id
                   FROM InvoicePaymentDetailEntity ipd
                   WHERE ipd.invoice.id = t.id
               )
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END DESC
            """)
    Page<Long> findByPeriod(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, Pageable pageable);

    @Query("""
            SELECT t.id
            FROM TransactionEntity t
            WHERE t.invoiceId IS NULL AND ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND t.value != 0
              AND NOT EXISTS (
                   SELECT DISTINCT ipd.invoice.id
                   FROM InvoicePaymentDetailEntity ipd
                   WHERE ipd.invoice.id = t.id
               )
            """)
    List<Long> findByPeriod(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal);

    @Query("""
            SELECT t.id
            FROM TransactionEntity t
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND ABS(t.value) = :value
              AND t.invoiceId IS NULL
              AND NOT EXISTS (
                   SELECT DISTINCT ipd.invoice.id
                   FROM InvoicePaymentDetailEntity ipd
                   WHERE ipd.invoice.id = t.id
              )
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END DESC
            """)
    Page<Long> findByPeriodAndValue(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("value") BigDecimal value, Pageable pageable);

    @Query("""
            SELECT t.id FROM TransactionEntity t
            JOIN t.subcategory sb
            JOIN sb.category c
            JOIN t.account
            LEFT JOIN t.card
            LEFT JOIN t.vehicle v
            LEFT JOIN v.brand
            LEFT JOIN v.model
            LEFT JOIN t.contract
            LEFT JOIN t.partner
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND (sb.description LIKE %:description% OR c.description LIKE %:description% OR t.description LIKE %:description%)
              AND t.invoiceId IS NULL
              AND t.value != 0
              AND NOT EXISTS (
                   SELECT DISTINCT ipd.invoice.id
                   FROM InvoicePaymentDetailEntity ipd
                   WHERE ipd.invoice.id = t.id
              )
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END DESC
            """)
    Page<Long> findByPeriodAndDescription(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("description") String description, Pageable pageable);

    @Query("""
            SELECT t.id
            FROM TransactionEntity t
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND t.account.id IN (:accounts)
              AND t.invoiceId IS NULL
              AND t.value != 0
              AND NOT EXISTS (
                   SELECT DISTINCT ipd.invoice.id
                   FROM InvoicePaymentDetailEntity ipd
                   WHERE ipd.invoice.id = t.id
              )
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END DESC
            """)
    Page<Long> findByPeriodAndAccount(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("accounts") List<Long> accounts, Pageable pageable);

    @Query("""
            SELECT t.id FROM TransactionEntity t
            JOIN t.subcategory sb
            JOIN sb.category c
            JOIN t.account acc
            LEFT JOIN t.card
            LEFT JOIN t.vehicle v
            LEFT JOIN v.brand
            LEFT JOIN v.model
            LEFT JOIN t.contract
            LEFT JOIN t.partner
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND (sb.description LIKE %:description% OR c.description LIKE %:description% OR t.description LIKE %:description%)
              AND t.account.id IN (:accounts)
              AND t.invoiceId IS NULL
              AND NOT EXISTS (
                   SELECT DISTINCT ipd.invoice.id
                   FROM InvoicePaymentDetailEntity ipd
                   WHERE ipd.invoice.id = t.id
              )
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END DESC
            """)
    Page<Long> findByPeriodAndAccountAndDescription(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("accounts") List<Long> accounts, @Param("description") String description, Pageable pageable);

    @Query("""
            SELECT t.id
            FROM TransactionEntity t
            WHERE ((t.paymentDate IS NULL AND t.dueDate BETWEEN :dtInitial AND :dtFinal) OR (t.paymentDate BETWEEN :dtInitial AND :dtFinal))
              AND ABS(t.value) = :value
              AND t.account.id IN (:accounts)
              AND t.invoiceId IS NULL
              AND NOT EXISTS (
                   SELECT DISTINCT ipd.invoice.id
                   FROM InvoicePaymentDetailEntity ipd
                   WHERE ipd.invoice.id = t.id
              )
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END DESC
            """)
    Page<Long> findByPeriodAndAccountAndValue(@Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal, @Param("accounts") List<Long> accounts, @Param("value") BigDecimal value, Pageable pageable);

    @Query("""
            SELECT t FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle v
            LEFT JOIN FETCH v.brand
            LEFT JOIN FETCH v.model
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE t.signature = :signature ORDER BY t.installment ASC
            """)
    List<TransactionEntity> findBySignature(@Param("signature") String signature);

    @NonNull
    @Override
    @Query("""
            SELECT t FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle v
            LEFT JOIN FETCH v.brand
            LEFT JOIN FETCH v.model
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE 1 = 1
              AND t.invoiceId IS NULL
              AND NOT EXISTS (
                   SELECT DISTINCT ipd.invoice.id
                   FROM InvoicePaymentDetailEntity ipd
                   WHERE ipd.invoice.id = t.id
              )
            ORDER BY t.id DESC
            """)
    List<TransactionEntity> findAll();

    @NonNull
    @Override
    @Query("""
            SELECT t FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle v
            LEFT JOIN FETCH v.brand
            LEFT JOIN FETCH v.model
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE t.id = :id
            """)
    Optional<TransactionEntity> findById(@NonNull Long id);

    @Query("SELECT t.signature FROM TransactionEntity t WHERE t.id = :id")
    String findBySignature(@Param("id") Long id);

    @Query("""
            SELECT t
            FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            LEFT JOIN FETCH t.vehicle v
            LEFT JOIN FETCH v.brand
            LEFT JOIN FETCH v.model
            LEFT JOIN FETCH t.contract
            LEFT JOIN FETCH t.partner
            WHERE t.id IN :ids
            ORDER BY CASE WHEN t.paymentDate IS NULL THEN t.dueDate ELSE t.paymentDate END DESC
            """)
    List<TransactionEntity> findByIdInWithDetails(@Param("ids") List<Long> ids);

    @Query("""
            SELECT t
            FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            WHERE t.invoice = false
              AND t.dueDate BETWEEN :dtInicial AND :dtFinal
              AND t.categoryType = :categoryType
            """)
    List<TransactionEntity> findBy(@Param("dtInicial") LocalDate dtInicial, @Param("dtFinal") LocalDate dtFinal,
                                   @Param("categoryType") String categoryType);
}
