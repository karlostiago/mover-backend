package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.TransactionService;
import com.ctsousa.mover.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionServiceImpl extends BaseServiceImpl<TransactionEntity, Long> implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    public TransactionServiceImpl(TransactionRepository repository) {
        super(repository);
    }

    @Override
    public TransactionEntity save(TransactionEntity entity) {
        return null;
    }

    @Override
    public TransactionEntity save(Transaction transaction) {
        TypeCategory typeCategory = TypeCategory.toDescription(transaction.getCategoryType());
        TransactionEntity entity;
        switch (typeCategory) {
            case INCOME -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case TRANSFER -> entity = transferService.transferBetweenAccount(transaction, repository);
            case EXPENSE -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case INVESTMENT -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case CORPORATE_CAPITAL -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            default -> throw new NotificationException("Transação não suportada!");
        }
        return entity;
    }

    @Override
    public TransactionEntity pay(Long id) {
        TransactionEntity entity = findById(id);
        TypeCategory typeCategory = TypeCategory.toDescription(entity.getCategoryType());
        switch (typeCategory) {
            case INCOME -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case TRANSFER -> transferService.pay(entity.getSignature(), repository);
            case EXPENSE -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case INVESTMENT -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case CORPORATE_CAPITAL -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            default -> throw new NotificationException("Transação não suportada!");
        }
        return entity;
    }

    @Override
    public TransactionEntity refund(Long id) {
        TransactionEntity entity = findById(id);
        TypeCategory typeCategory = TypeCategory.toDescription(entity.getCategoryType());
        switch (typeCategory) {
            case INCOME -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case TRANSFER -> transferService.refund(entity.getSignature(), repository);
            case EXPENSE -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case INVESTMENT -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case CORPORATE_CAPITAL -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            default -> throw new NotificationException("Transação não suportada!");
        }
        return entity;
    }

    @Override
    public BigDecimal balance(final Boolean scrowAccount) {
        List<Long> accounts = accountService.findByAccount(scrowAccount)
                .stream().map(AccountEntity::getId)
                .toList();

        return repository.balance(accounts, scrowAccount);
    }
}
