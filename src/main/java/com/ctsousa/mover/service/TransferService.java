package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.response.TransactionResponse;

public interface TransferService {

    TransactionEntity betweenAccount(final Transaction transaction);

    TransactionEntity update(final Transaction transaction);

    TransactionResponse searchById(final Long id);
}
