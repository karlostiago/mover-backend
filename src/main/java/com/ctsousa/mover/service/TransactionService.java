package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService extends BaseService<TransactionEntity, Long> {

    TransactionEntity save(final Transaction transaction);

    TransactionResponse update(final Long id, Transaction transaction);

    TransactionEntity pay(final Long id, LocalDate paymentDate);

    TransactionEntity refund(final Long id);

    BigDecimal accountBalace(final List<Long> listAccountId);

    BigDecimal incomeBalance(final List<TransactionEntity> entities);

    BigDecimal expenseBalance(final List<TransactionEntity> entities);

    TransactionResponse searchById(final Long id);

    void deleteById(Long id, Boolean deleteOnlyThis);

    Page<TransactionEntity> find(final LocalDate dtInitial, final LocalDate dtFinal, List<Long> accountListId, String text, Pageable pageable);
}
