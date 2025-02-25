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
            SELECT
            	COALESCE(SUM(TEMP.BALANCE), 0) AS BALANCE
            FROM (
            	SELECT
            	    c.name AS NAME,
            	    COALESCE(c.initial_balance, 0) + COALESCE((
            	        SELECT SUM(t.value)
            	        FROM tb_transaction t
            	        WHERE t.account_id = c.id
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
    @Query(value = "SELECT SUM(t.value * -1) AS DESPESA FROM tb_transaction t WHERE t.category_type = 'EXPENSE'", nativeQuery = true)
    BigDecimal expenseBalance();

    @Deprecated
    @Query(value = "SELECT SUM(t.value) AS RECEITA FROM tb_transaction t WHERE t.category_type = 'INCOME'", nativeQuery = true)
    BigDecimal incomeBalance();
}
