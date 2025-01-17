package com.ctsousa.mover.core.factory;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;

public interface TransactionExecutor {
    TransactionEntity execute(final TypeCategory type, final Transaction transaction);
}
