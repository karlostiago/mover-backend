package com.ctsousa.mover.core.factory;

import com.ctsousa.mover.core.command.TransactionCommand;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DeleteTransactionServiceFactory extends AbstractTransactionServiceFactory {

    private Boolean batchDelete = Boolean.FALSE;

    public DeleteTransactionServiceFactory(Map<TypeCategory, TransactionCommand> services) {
        super(services);
    }

    @Override
    public TransactionEntity execute(final TypeCategory type, final Transaction transaction) {
        if (batchDelete) {
            execute(type).batchDelete(transaction.getId());
        } else {
            execute(type).delete(transaction.getId());
        }
        return new TransactionEntity();
    }

    public void batchDelete(boolean batchDelete) {
        this.batchDelete = batchDelete;
    }
}
