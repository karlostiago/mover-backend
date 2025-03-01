package com.ctsousa.mover.core.factory;

import com.ctsousa.mover.core.command.TransactionCommand;
import com.ctsousa.mover.enumeration.TypeCategory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public abstract class AbstractTransactionServiceFactory implements TransactionExecutor {

    private final Map<TypeCategory, TransactionCommand> services;

    @Autowired
    public AbstractTransactionServiceFactory(Map<TypeCategory, TransactionCommand> services) {
        this.services = services;
    }

    protected TransactionCommand execute(TypeCategory type) {
        return this.services.get(type);
    }
}
