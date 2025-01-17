package com.ctsousa.mover.service;

import com.ctsousa.mover.core.command.TransactionCommand;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;

public interface ExpenseService extends TransactionCommand {

    TransactionEntity update(Transaction transaction);
}
