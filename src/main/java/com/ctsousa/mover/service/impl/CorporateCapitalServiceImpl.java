package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.CorporateCapitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CorporateCapitalServiceImpl extends BaseTransactionServiceImpl implements CorporateCapitalService {

    @Autowired
    private AccountService accountService;

    public CorporateCapitalServiceImpl(TransactionRepository repository, AccountService accountService) {
        super(repository, accountService);
//        this.accountService = accountService;
    }

    @Override
    public TransactionEntity contribuition(Transaction transaction) {
        TransactionEntity entity = transaction.toEntity();
        entity.setTransactionType(TransactionType.CREDIT.name());

        if (entity.getPaid()) {
            updateBalance(entity.getAccount(), entity.getValue());
        }

        return repository.save(entity);
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        return null;
    }
}
