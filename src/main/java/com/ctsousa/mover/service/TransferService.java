package com.ctsousa.mover.service;

import com.ctsousa.mover.core.command.TransactionCommand;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.response.TransactionResponse;

public interface TransferService extends TransactionCommand {

    TransactionEntity update(final Transaction transaction);

    TransactionResponse searchById(final Long id);

    void delete(final TransactionEntity entity, Boolean deleteOnlyThis);

    void payOrRefund(TransactionEntity entity, Boolean pay);
}
