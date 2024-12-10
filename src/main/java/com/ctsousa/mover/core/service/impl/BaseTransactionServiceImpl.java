package com.ctsousa.mover.core.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

public class BaseTransactionServiceImpl extends BaseServiceImpl<TransactionEntity, Long> {

    private final AccountService accountService;

    public BaseTransactionServiceImpl(TransactionRepository repository, AccountService accountService) {
        super(repository);
        this.accountService = accountService;
    }

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

    public void updateBalance(AccountEntity account, BigDecimal value) {
        BigDecimal balance = account.getAvailableBalance().add(value);
        account.setAvailableBalance(balance);
        accountService.save(account);
    }
}
