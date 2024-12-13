package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.mapper.Transform;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.CorporateCapitalService;
import com.ctsousa.mover.service.TransactionService;
import com.ctsousa.mover.service.TransferService;
import jakarta.transaction.NotSupportedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@Component
public class TransactionServiceImpl extends BaseTransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CorporateCapitalService corporateCapitalService;


    public TransactionServiceImpl(TransactionRepository repository, AccountService accountService) {
        super(repository, accountService);
    }

    @Override
    public TransactionEntity save(TransactionEntity entity) {
        throw new NotificationException("Operação não suportada!", Severity.ERROR);
    }

    @Override
    public TransactionEntity save(Transaction transaction) {
        return switch (getTypeCategory(transaction.getCategoryType())) {
            case INCOME -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case TRANSFER -> transferService.betweenAccount(transaction);
            case EXPENSE -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case INVESTMENT -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case CORPORATE_CAPITAL -> corporateCapitalService.contribuition(transaction);
        };
    }

    @Override
    public TransactionEntity pay(Long id) {
        TransactionEntity entity = findById(id);
        switch (getTypeCategory(entity.getCategoryType())) {
            case INCOME -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case TRANSFER -> transferService.pay(entity.getSignature());
            case EXPENSE -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case INVESTMENT -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case CORPORATE_CAPITAL -> corporateCapitalService.pay(entity.getSignature());
            default -> throw new NotificationException("Transação não suportada!");
        }
        return entity;
    }

    @Override
    public TransactionEntity refund(Long id) {
        TransactionEntity entity = findById(id);
        switch (getTypeCategory(entity.getCategoryType())) {
            case INCOME -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case TRANSFER -> transferService.refund(entity.getSignature());
            case EXPENSE -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case INVESTMENT -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case CORPORATE_CAPITAL -> corporateCapitalService.refund(entity.getSignature());
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

    @Override
    public TransactionResponse update(Long id, Transaction transaction) {
        return switch (getTypeCategory(transaction.getCategoryType())) {
            case INCOME -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case TRANSFER -> toMapper(transferService.update(transaction), TransactionResponse.class);
            case EXPENSE -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case INVESTMENT -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case CORPORATE_CAPITAL -> toMapper(corporateCapitalService.update(transaction), TransactionResponse.class);
        };
    }

    @Override
    public TransactionResponse searchById(Long id) {
        TransactionEntity entity = findById(id);
        return switch (getTypeCategory(entity.getCategoryType())) {
            case INCOME -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case TRANSFER -> transferService.searchById(id);
            case EXPENSE -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case INVESTMENT -> throw new NotificationException("Operação não suportada.", Severity.WARNING);
            case CORPORATE_CAPITAL -> toMapper(entity, TransactionResponse.class);
        };
    }

    @Override
    public List<TransactionEntity> findAll() {
        List<TransactionEntity> entities = new ArrayList<>();
        for (TransactionEntity entity : repository.findAll()) {
            if (ignoreTransaction(entity)) {
                continue;
            }
            entities.add(entity);
        }
        return entities;
    }

    private boolean ignoreTransaction(TransactionEntity entity) {
        return "CREDIT".equals(entity.getTransactionType()) && "TRANSFER".equals(entity.getCategoryType());
    }

    private TypeCategory getTypeCategory(String type) {
        return TypeCategory.toDescription(type);
    }
}
