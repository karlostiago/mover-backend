package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.CorporateCapitalService;
import org.springframework.stereotype.Component;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class CorporateCapitalServiceImpl extends BaseTransactionServiceImpl implements CorporateCapitalService {

    private final AccountService accountService;

    public CorporateCapitalServiceImpl(TransactionRepository repository, AccountService accountService) {
        super(repository, accountService);
        this.accountService = accountService;
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
        TransactionEntity originalTransaction = findById(transaction.getId());
        String signature = originalTransaction.getSignature();

        boolean wasPaid = originalTransaction.getPaid();
        boolean isNowPaid = transaction.getPaid();

        TransactionEntity entity = transaction.toEntity();
        entity.setTransactionType(TransactionType.CREDIT.name());
        entity.setAccount(accountService.findById(transaction.getAccount().getId()));
        entity.setSignature(signature);

        if (wasPaid && !isNowPaid) {
            updateBalance(entity.getAccount(), invertSignal(transaction.getValue()));
        } else if (!wasPaid && isNowPaid) {
            updateBalance(entity.getAccount(), transaction.getValue());
        }

        return repository.save(entity);
    }

    @Override
    public void delete(TransactionEntity entity) {
        super.delete(entity);
    }
}
