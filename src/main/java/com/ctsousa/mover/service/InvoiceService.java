package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface InvoiceService extends BaseService<TransactionEntity, Long> {

    List<TransactionEntity> searchById(Long id);

    TransactionEntity toGenerate(TransactionEntity entity);

    TransactionEntity update(TransactionEntity invoice, TransactionEntity entity);

    TransactionEntity schedule(final Long id);

    TransactionEntity undoScheduling(final Long id);

    TransactionEntity pay(final Long id, LocalDate paymentDate, final BigDecimal value, final AccountEntity account);

    TransactionEntity refund(final Long id);

    void delete(TransactionEntity entity);

    TransactionEntity next(Long cardId, LocalDate dueDate);

    TransactionEntity previous(Long cardId, LocalDate dueDate);
}
