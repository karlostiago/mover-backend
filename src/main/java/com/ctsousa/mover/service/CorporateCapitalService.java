package com.ctsousa.mover.service;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;

public interface CorporateCapitalService {

    TransactionEntity contribuition(final Transaction transaction);

    TransactionEntity update(final Transaction transaction);
}
