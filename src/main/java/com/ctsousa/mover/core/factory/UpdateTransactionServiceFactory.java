package com.ctsousa.mover.core.factory;

import com.ctsousa.mover.core.command.TransactionCommand;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UpdateTransactionServiceFactory extends AbstractTransactionServiceFactory {

    private Boolean batchUpdate = Boolean.FALSE;

    public UpdateTransactionServiceFactory(Map<TypeCategory, TransactionCommand> services) {
        super(services);
    }

    @Override
    public TransactionEntity execute(final TypeCategory type, final Transaction transaction) {
        if (batchUpdate) {
            return services.get(type).batchUpdate(transaction);
        } else {
            return services.get(type).update(transaction);
        }
    }

    public void batchUpdate(boolean batchUpdate) {
        this.batchUpdate = batchUpdate;
    }
}
