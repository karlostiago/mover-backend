package com.ctsousa.mover.core.factory;

import com.ctsousa.mover.core.command.TransactionCommand;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CreateTransactionServiceFactory extends AbstractTransactionServiceFactory {

    public CreateTransactionServiceFactory(Map<TypeCategory, TransactionCommand> services) {
        super(services);
    }

    @Override
    public TransactionEntity execute(final TypeCategory type, final Transaction transaction) {
        return services.get(type).save(transaction);
    }
}
