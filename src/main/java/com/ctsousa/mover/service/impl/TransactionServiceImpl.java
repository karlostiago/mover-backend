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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;
import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

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

    @Autowired
    private InstallmentService installmentService;

    @Autowired
    private FixedInstallmentService fixedInstallmentService;

    public TransactionServiceImpl(TransactionRepository repository, AccountService accountService, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, accountService, installmentService, fixedInstallmentService);
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
            case TRANSFER -> transferService.payOrRefund(entity, true);
            case CORPORATE_CAPITAL, INCOME, EXPENSE, INVESTMENT -> pay(entity);
            default -> throw new NotificationException("Transação não suportada!");
        }
        return entity;
    }

    @Override
    public TransactionEntity refund(Long id) {
        TransactionEntity entity = findById(id);
        switch (getTypeCategory(entity.getCategoryType())) {
            case TRANSFER -> transferService.payOrRefund(entity, false);
            case CORPORATE_CAPITAL, INCOME, EXPENSE, INVESTMENT -> refund(entity);
            default -> throw new NotificationException("Transação não suportada!");
        }
        return entity;
    }

    @Override
    public BigDecimal accountBalace() {
        List<Long> listId = new ArrayList<>(accountService.findAll()
                .stream().map(AccountEntity::getId)
                .toList());
        return repository.accountBalance(listId);
    }

    @Override
    public BigDecimal incomeBalance() {
        BigDecimal incomeBalance = repository.incomeBalance();
        return incomeBalance == null ? BigDecimal.ZERO : incomeBalance;
    }

    @Override
    public BigDecimal expenseBalance() {
        BigDecimal expenseBalance = repository.expenseBalance();
        return expenseBalance == null ? BigDecimal.ZERO : expenseBalance;
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
    public void deleteById(Long id, Boolean deleteOnlyThis) {
        TransactionEntity entity = findById(id);
        switch (getTypeCategory(entity.getCategoryType())) {
            case TRANSFER -> transferService.delete(entity, deleteOnlyThis);
            case CORPORATE_CAPITAL, EXPENSE, INCOME, INVESTMENT -> delete(entity, deleteOnlyThis);
            default -> throw new NotificationException(entity.getCategoryType() + " :: Operação não suportada");
        }
    }

    @Override
    public List<TransactionEntity> findAll() {
        List<TransactionEntity> entities = new ArrayList<>();
        for (TransactionEntity entity : repository.findAll()) {
            if (isIgnoreTransaction(entity)) {
                continue;
            }
            entities.add(entity);
        }
        return entities;
    }

    @Override
    public List<TransactionEntity> find(LocalDate dtInitial, LocalDate dtFinal, List<Long> accountListId, String text) {
        List<TransactionEntity> entities = repository.findByPeriod(dtInitial, dtFinal);

        if (accountListId.isEmpty() && text == null) {
            return entities.stream().filter(this::isNotIgnoreTransaction)
                    .toList();
        }

        List<TransactionEntity> entitiesFindByAccounts = null;
        if (!accountListId.isEmpty()) {
            entitiesFindByAccounts = findTransactionByAccount(accountListId, entities);
        }
        List<TransactionEntity> entitiesFindByText = null;
        if (text != null && !text.isEmpty()) {
            entitiesFindByText = findTransactionByText(text, entities);
        }

        List<TransactionEntity> entitiesFiltered = new ArrayList<>(entities.size());
        if (entitiesFindByAccounts != null && !entitiesFindByAccounts.isEmpty()) {
            entitiesFiltered.addAll(entitiesFindByAccounts);
        }

        if (entitiesFindByText != null && !entitiesFindByText.isEmpty()) {
            entitiesFiltered.addAll(entitiesFindByText);
        }

        return entitiesFiltered;
    }

    private List<TransactionEntity> findTransactionByText(final String text, final List<TransactionEntity> entities) {
        List<TransactionEntity> entitiesFiltered = new ArrayList<>(entities.size());

        var textUpper = toUppercase(text);
        var transactionValue = NumberUtil.parseMonetary(text);

        if (transactionValue != null) {
            textUpper = null;
        }

        for (TransactionEntity entity : entities) {
            if (isIgnoreTransaction(entity)) continue;
            if (textUpper != null && entity.getDescription().contains(textUpper)) {
                entitiesFiltered.add(entity);
            } else if (textUpper != null && entity.getSubcategory().getDescription().contains(textUpper)) {
                entitiesFiltered.add(entity);
            } else if (textUpper != null && entity.getSubcategory().getCategory().getDescription().contains(textUpper)) {
                entitiesFiltered.add(entity);
            } else if (transactionValue != null && entity.getValue().abs().equals(transactionValue.abs())) {
                entitiesFiltered.add(entity);
            }
        }

        return entitiesFiltered;
    }

    private List<TransactionEntity> findTransactionByAccount(final List<Long> accountListId, final List<TransactionEntity> entities) {
        List<TransactionEntity> entitiesFiltered = new ArrayList<>(entities.size());

        if (!accountListId.isEmpty()) {
            for (Long accountId : accountListId) {
                for (TransactionEntity entity : entities) {
                    if (isIgnoreTransaction(entity)) continue;
                    if (entity.getAccount().getId().equals(accountId)) {
                        entitiesFiltered.add(entity);
                    }
                }
            }
        }

        return entitiesFiltered;
    }

    private boolean isIgnoreTransaction(TransactionEntity entity) {
        return "CREDIT".equals(entity.getTransactionType()) && "TRANSFER".equals(entity.getCategoryType());
    }

    private boolean isNotIgnoreTransaction(TransactionEntity entity) {
        return !isIgnoreTransaction(entity);
    }

    private TypeCategory getTypeCategory(String type) {
        return TypeCategory.toDescription(type);
    }
}
