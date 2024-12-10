package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransferServiceImpl extends BaseTransactionServiceImpl implements TransferService {

    @Autowired
    private final AccountService accountService;

    public TransferServiceImpl(TransactionRepository repository, AccountService accountService) {
        super(repository, accountService);
        this.accountService = accountService;
    }

    @Override
    public TransactionEntity betweenAccount(Transaction transaction, TransactionRepository repository) {
        AccountEntity creditAccount = accountService.findById(transaction.getDestinationAccount().getId());
        AccountEntity debitAccount = accountService.findById(transaction.getAccount().getId());

        TransactionEntity entity = transaction.toEntity();

        entity.setTransactionType(TransactionType.CREDIT.name());
        entity.setAccount(creditAccount);
        repository.save(entity);

        if (entity.getPaid()) {
            updateBalance(creditAccount, entity.getValue());
        }

        entity.setTransactionType(TransactionType.DEBIT.name());
        entity.setAccount(debitAccount);
        entity.setValue(entity.getValue().multiply(BigDecimal.valueOf(-1)));
        repository.save(entity);

        if (entity.getPaid()) {
            updateBalance(debitAccount, entity.getValue());
        }

        return entity;
    }

    /*
    @Override
    public void pay(final String signature, TransactionRepository repository) {
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            if (entity.getPaid()) continue;
            entity.setPaid(true);
            entity.setPaymentDate(entity.getPaymentDate() != null ? entity.getPaymentDate() : entity.getDueDate());
            updateBalance(entity.getAccount(), entity.getValue());
            repository.save(entity);
        }
    }

    @Override
    public void refund(String signature, TransactionRepository repository) {
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            if (!entity.getPaid()) continue;
            entity.setPaid(false);
            entity.setRefund(true);
            updateBalance(entity.getAccount(), invertSignal(entity.getValue()));
            repository.save(entity);
        }
    }

    private void updateBalance(AccountEntity account, BigDecimal value) {
        BigDecimal balance = account.getAvailableBalance().add(value);
        account.setAvailableBalance(balance);
        accountService.save(account);
    }
    */
}
