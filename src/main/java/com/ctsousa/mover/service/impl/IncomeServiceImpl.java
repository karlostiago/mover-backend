package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class IncomeServiceImpl extends BaseTransactionServiceImpl implements IncomeService {

    @Autowired
    private final AccountService accountService;

    public IncomeServiceImpl(TransactionRepository repository, AccountService accountService) {
        super(repository, accountService, null, null);
        this.accountService = accountService;
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        TransactionEntity originalTransaction = findById(transaction.getId());
        String signature = originalTransaction.getSignature();

        boolean wasPaid = originalTransaction.getPaid();
        boolean isNowPaid = transaction.getPaid();

        TransactionEntity entity = transaction.toEntity();
        entity.setTransactionType(TransactionType.DEBIT.name());
        entity.setAccount(accountService.findById(transaction.getAccount().getId()));
        entity.setSignature(signature);
        entity.setValue(invertSignal(transaction.getValue()));

        if (wasPaid && !isNowPaid) {
            updateBalance(entity.getAccount(), transaction.getValue());
        } else if (!wasPaid && isNowPaid) {
            updateBalance(entity.getAccount(), invertSignal(transaction.getValue()));
        }

        return repository.save(entity);
    }
}
