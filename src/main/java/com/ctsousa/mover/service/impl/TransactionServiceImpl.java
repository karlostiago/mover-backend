package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.exception.severity.Severity;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;
import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;
import static com.ctsousa.mover.core.util.NumberUtil.parseMonetary;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
    public TransactionEntity pay(Long id, LocalDate paymentDate) {
        TransactionEntity entity = findById(id);
        entity.setPaymentDate(paymentDate);

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
        return repository.findAll().stream()
                .filter(this::isNotIgnoreTransaction)
                .toList();
    }

    @Override
    public Page<TransactionEntity> find(LocalDate dtInitial, LocalDate dtFinal, List<Long> accountListId, String text, Pageable pageable) {
        var value = parseMonetary(text);
        text = value != null ? null : toUppercase(text);
        Page<TransactionEntity> page;
        if (hasAccountAndText(accountListId, text)) {
            page = repository.findByPeriodAndAccountAndDescription(dtInitial, dtFinal, accountListId, text, pageable);
            List<TransactionEntity> entities = page.stream().filter(this::isNotIgnoreTransaction).toList();
            return new PageImpl<>(entities, pageable, page.getTotalElements());
        }
        if (hasAccountAndValue(accountListId, value)) {
            page = repository.findByPeriodAndAccountAndValue(dtInitial, dtFinal, accountListId, value, pageable);
            List<TransactionEntity> entities = page.stream().filter(this::isNotIgnoreTransaction).toList();
            return new PageImpl<>(entities, pageable, page.getTotalElements());
        }
        if (hasValueAndNotAccount(accountListId, value)) {
            page = repository.findByPeriodAndValue(dtInitial, dtFinal, value, pageable);
            List<TransactionEntity> entities = page.stream().filter(this::isNotIgnoreTransaction).toList();
            return new PageImpl<>(entities, pageable, page.getTotalElements());
        }
        if (hasTextAndNotAccount(accountListId, text)) {
            page = repository.findByPeriodAndDescription(dtInitial, dtFinal, text, pageable);
            List<TransactionEntity> entities = page.stream().filter(this::isNotIgnoreTransaction).toList();
            return new PageImpl<>(entities, pageable, page.getTotalElements());
        }
        if (hasAccount(accountListId)) {
            page = repository.findByPeriodAndAccount(dtInitial, dtFinal, accountListId, pageable);
            List<TransactionEntity> entities = page.stream().filter(this::isNotIgnoreTransaction).toList();
            return new PageImpl<>(entities, pageable, page.getTotalElements());
        }
        page = repository.findByPeriod(dtInitial, dtFinal, pageable);
        List<TransactionEntity> entities = page.stream().filter(this::isNotIgnoreTransaction).toList();
        return new PageImpl<>(entities, pageable, page.getTotalElements());
    }

    private boolean hasAccountAndText(List<Long> accountListId, String text) {
        return !accountListId.isEmpty() && isNotEmpty(text);
    }

    private boolean hasAccountAndValue(List<Long> accountListId, BigDecimal value) {
        return !accountListId.isEmpty() && value != null;
    }

    private boolean hasValueAndNotAccount(List<Long> accountListId, BigDecimal value) {
        return accountListId.isEmpty() && value != null;
    }

    private boolean hasTextAndNotAccount(List<Long> accountListId, String text) {
        return accountListId.isEmpty() && isNotEmpty(text);
    }

    private boolean hasAccount(List<Long> accountListId) {
        return !accountListId.isEmpty();
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
