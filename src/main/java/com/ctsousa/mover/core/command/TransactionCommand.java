package com.ctsousa.mover.core.command;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;

import java.time.LocalDate;

public interface TransactionCommand {

    TransactionEntity save(Transaction transaction);
    TransactionEntity update(Transaction transaction);
    TransactionEntity filterById(Long id);
    TransactionEntity payment(Long id, LocalDate paymentDate);
    TransactionEntity refund(Long id);
    void delete(Long id);
}
