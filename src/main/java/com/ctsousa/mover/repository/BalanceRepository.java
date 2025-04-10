package com.ctsousa.mover.repository;

import com.ctsousa.mover.core.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BalanceRepository extends JpaRepository<TransactionEntity, Long> {

    @Query(value = """
            SELECT SUM(c.initial_balance + IFNULL(t.total_value, 0)) AS BALANCE
            FROM tb_account c
            LEFT JOIN (
                SELECT t.account_id, SUM(t.value) AS total_value
                FROM tb_transaction t
                WHERE t.paid
                AND t.invoice_id IS NULL
                AND NOT EXISTS (
                  SELECT DISTINCT ipd.invoice_id
                  FROM tb_invoice_payment_detail ipd
                  WHERE ipd.invoice_id = t.id
                )
                GROUP BY t.account_id
            ) t ON t.account_id = c.id
            """, nativeQuery = true)
    BigDecimal accountBalance(@Param("accounts") List<Long> accounts);

    @Deprecated
    @Query(value = "SELECT SUM(t.value * -1) AS DESPESA FROM tb_transaction t WHERE AND t.invoice_id IS NUL AND t.category_type = 'EXPENSE'", nativeQuery = true)
    BigDecimal expenseBalance();

    @Deprecated
    @Query(value = "SELECT SUM(t.value) AS RECEITA FROM tb_transaction t WHERE AND t.invoice_id IS NUL AND t.category_type = 'INCOME'", nativeQuery = true)
    BigDecimal incomeBalance();
}
