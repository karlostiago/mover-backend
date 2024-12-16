package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.core.util.NumberUtil;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;
import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class TransactionServiceImpl extends BaseTransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CardService cardService;

    @Autowired
    private IncomeService incomeService;

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
            case INCOME, CORPORATE_CAPITAL -> save(transaction, TransactionType.CREDIT);
            case TRANSFER -> transferService.betweenAccount(transaction);
            case EXPENSE, INVESTMENT -> save(transaction, TransactionType.DEBIT);
        };
    }

    @Override
    public TransactionEntity pay(Long id) {
        TransactionEntity entity = findById(id);
        switch (getTypeCategory(entity.getCategoryType())) {
            case TRANSFER, CORPORATE_CAPITAL, INCOME, EXPENSE, INVESTMENT -> pay(entity.getSignature());
            default -> throw new NotificationException("Transação não suportada!");
        }
        return entity;
    }

    @Override
    public TransactionEntity refund(Long id) {
        TransactionEntity entity = findById(id);
        switch (getTypeCategory(entity.getCategoryType())) {
            case TRANSFER, CORPORATE_CAPITAL, INCOME, EXPENSE, INVESTMENT -> refund(entity.getSignature());
            default -> throw new NotificationException("Transação não suportada!");
        }
        return entity;
    }

    @Override
    public BigDecimal balance(final Boolean escrowAccount) {
        List<Long> listId = new ArrayList<>(accountService.findByAccount(escrowAccount)
                .stream().map(AccountEntity::getId)
                .toList());

        if (escrowAccount == null) {
            cardService.findAll().forEach(c -> listId.add(c.getId()));
            return repository.creditBalance(listId);
        }

        return repository.balance(listId, escrowAccount);
    }

    @Override
    public TransactionResponse update(Long id, Transaction transaction) {
        return switch (getTypeCategory(transaction.getCategoryType())) {
            case INCOME, CORPORATE_CAPITAL -> toMapper(update(transaction, TransactionType.CREDIT), TransactionResponse.class);
            case TRANSFER -> toMapper(transferService.update(transaction), TransactionResponse.class);
            case EXPENSE, INVESTMENT -> toMapper(incomeService.update(transaction), TransactionResponse.class);
        };
    }

    @Override
    public TransactionResponse searchById(Long id) {
        TransactionEntity entity = findById(id);
        entity.setValue(isDebit(entity) ? invertSignal(entity.getValue()) : entity.getValue());
        return switch (getTypeCategory(entity.getCategoryType())) {
            case INCOME, CORPORATE_CAPITAL, EXPENSE, INVESTMENT -> toMapper(entity, TransactionResponse.class);
            case TRANSFER -> transferService.searchById(id);
        };
    }

    @Override
    public void deleteById(Long id) {
        TransactionEntity entity = findById(id);
        switch (getTypeCategory(entity.getCategoryType())) {
            case TRANSFER, CORPORATE_CAPITAL, EXPENSE, INCOME, INVESTMENT -> delete(entity);
            default -> throw new NotificationException(entity.getCategoryType() + " :: Operação não suportada");
        }
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
