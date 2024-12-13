package com.ctsousa.mover.core.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

public class BaseTransactionServiceImpl extends BaseServiceImpl<TransactionEntity, Long> {

    private final AccountService accountService;

    protected final TransactionRepository repository;

    public BaseTransactionServiceImpl(TransactionRepository repository, AccountService accountService) {
        super(repository);
        this.repository = repository;
        this.accountService = accountService;
    }

    public void pay(final String signature) {
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            if (entity.getPaid()) continue;
            entity.setPaid(true);
            entity.setPaymentDate(LocalDate.now());
            updateBalance(entity.getAccount(), entity.getValue());
            repository.save(entity);
        }
    }

    public void refund(String signature) {
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            if (!entity.getPaid()) continue;
            entity.setPaid(false);
            entity.setRefund(true);
            entity.setPaymentDate(null);
            updateBalance(entity.getAccount(), invertSignal(entity.getValue()));
            repository.save(entity);
        }
    }

    public void delete(TransactionEntity entity) {
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
        for (TransactionEntity transaction : entities) {
            if (entity.getPaid()) {
                updateBalance(transaction.getAccount(), invertSignal(transaction.getValue()));
            }
            super.deleteById(transaction.getId());
        }
    }

    public void updateBalance(AccountEntity account, BigDecimal value) {
        BigDecimal balance = account.getAvailableBalance().add(value);
        account.setAvailableBalance(balance);
        accountService.save(account);
    }
}
