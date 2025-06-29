package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.projection.TransactionBalanceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionBalanceRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = """
            SELECT
                T_DATE AS 'date',
                SUM(VALUE) AS balance
              FROM (
                SELECT
                    tt.due_date AS T_DATE,
                    tt.value
                FROM tb_transaction tt
                JOIN tb_account ta ON ta.id IN (:accounts)
                WHERE tt.payment_date IS NULL
                  AND tt.paid = false
                  AND tt.due_date BETWEEN :dtInitial AND :dtFinal
                  AND tt.invoice_id IS NULL
                  AND tt.account_id = ta.id
                  AND NOT EXISTS (
                    SELECT 1 FROM tb_invoice_payment_detail ipd WHERE ipd.invoice_id = tt.id
                  )
              UNION ALL
                SELECT
                  tt.payment_date AS T_DATE,
                  tt.value
                FROM tb_transaction tt
                JOIN tb_account ta ON ta.id IN (:accounts)
                WHERE tt.payment_date BETWEEN :dtInitial AND :dtFinal
                  AND tt.invoice_id IS NULL
                  AND tt.account_id = ta.id
                  AND NOT EXISTS (
                    SELECT 1 FROM tb_invoice_payment_detail ipd WHERE ipd.invoice_id = tt.id
                  )
              ) AS TBL_TMP
              GROUP BY T_DATE
            """, nativeQuery = true)
    List<TransactionBalanceProjection> balance(@Param("accounts") List<Long> accounts, @Param("dtInitial") LocalDate dtInitial, @Param("dtFinal") LocalDate dtFinal);
}
