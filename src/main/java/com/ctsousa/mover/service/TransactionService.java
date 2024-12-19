package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.response.TransactionResponse;

import java.math.BigDecimal;

public interface TransactionService extends BaseService<TransactionEntity, Long> {

    TransactionEntity save(final Transaction transaction);

    TransactionResponse update(final Long id, Transaction transaction);

    TransactionEntity pay(final Long id);

    TransactionEntity refund(final Long id);

    BigDecimal accountBalace();

    BigDecimal incomeBalance();

    BigDecimal expenseBalance();

    TransactionResponse searchById(final Long id);
}
