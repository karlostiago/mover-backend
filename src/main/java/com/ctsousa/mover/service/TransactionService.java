package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.domain.Transaction;

import java.math.BigDecimal;

public interface TransactionService extends BaseService<TransactionEntity, Long> {

    TransactionEntity save(final Transaction domain);

    TransactionEntity pay(final Long id);

    TransactionEntity refund(final Long id);

    BigDecimal balance(final Boolean scrowAccount);
}
