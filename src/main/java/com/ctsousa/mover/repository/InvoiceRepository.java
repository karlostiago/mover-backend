package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<TransactionEntity, Long> {

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
            WHERE t.id = :id AND t.invoice = true AND t.invoiceId IS NULL
            UNION
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
            WHERE t.invoiceId = :id AND t.invoice = false
            ORDER BY t.registerDate DESC
            """)
    List<TransactionEntity> findBy(@Param("id") Long id);

    @Query("""
            SELECT t
            FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            WHERE t.dueDate = :dueDate
              AND t.invoice = true
              AND t.invoiceId IS NULL
              AND t.card = :card
              AND t.paymentDate IS NULL
            """)
    TransactionEntity findBy(@Param("dueDate") LocalDate dueDate, @Param("card") CardEntity card);

    @Query("""
            SELECT t
            FROM TransactionEntity t
            JOIN FETCH t.subcategory sb
            JOIN FETCH sb.category
            JOIN FETCH t.account
            LEFT JOIN FETCH t.card
            WHERE t.dueDate = :dueDate
              AND t.invoice = true
              AND t.invoiceId IS NULL
              AND t.card = :card
            ORDER BY t.id ASC
            """)
    List<TransactionEntity> next(@Param("dueDate") LocalDate dueDate, @Param("card") CardEntity card);
}
