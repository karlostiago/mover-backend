package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.repository.TransactionRepository;

public interface TransferService {

    TransactionEntity betweenAccount(final Transaction transaction, TransactionRepository repository);

    void pay(final String signature, TransactionRepository repository);

    void refund(final String signature, TransactionRepository repository);
}
