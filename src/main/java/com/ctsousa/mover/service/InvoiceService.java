package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.BaseService;
import com.ctsousa.mover.domain.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService extends BaseService<TransactionEntity, Long> {

    List<TransactionEntity> searchById(Long id);

    TransactionEntity updateItem(Long id, TransactionEntity invoiceItem, boolean sendNotify);

    TransactionEntity schedule(final Long id);

    TransactionEntity undoScheduling(final Long id);

    TransactionEntity pay(Transaction transaction);

    TransactionEntity refund(final Long id);

    void delete(TransactionEntity entity);

    TransactionEntity next(Long cardId, LocalDate dueDate);

    TransactionEntity previous(Long cardId, LocalDate dueDate);

    Boolean exists(LocalDate dueDate, CardEntity card);

    void updateBalance(LocalDate dueDate, CardEntity card);
}
