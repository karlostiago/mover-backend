package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.factory.*;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import com.ctsousa.mover.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.parseMonetary;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
public class TransactionServiceImpl extends BaseTransactionServiceImpl implements TransactionService {

    @Autowired
    private CreateTransactionServiceFactory createTransactionServiceFactory;

    @Autowired
    private UpdateTransactionServiceFactory updateTransactionServiceFactory;

    @Autowired
    private FilterTransactionServiceFactory filterTransactionServiceFactory;

    @Autowired
    private PaymentTransactionServiceFactory paymentTransactionServiceFactory;

    @Autowired
    private RefundTransactionServiceFactory refundTransactionServiceFactory;

    @Autowired
    private DeleteTransactionServiceFactory deleteTransactionServiceFactory;

    public TransactionServiceImpl(TransactionRepository repository, AccountService accountService, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, accountService, installmentService, fixedInstallmentService);
    }

    @Override
    public TransactionEntity save(Transaction transaction) {
        TypeCategory type = TypeCategory.toDescription(transaction.getCategoryType());
        return createTransactionServiceFactory.execute(type, transaction);
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        TypeCategory type = TypeCategory.toDescription(transaction.getCategoryType());
        updateTransactionServiceFactory.batchUpdate(false);
        return updateTransactionServiceFactory.execute(type, transaction);
    }

    @Override
    public TransactionEntity filterById(Long id, TypeCategory type) {
        return filterTransactionServiceFactory.execute(type, new Transaction(id));
    }

    @Override
    public TransactionEntity pay(Long id, LocalDate paymentDate) {
        TransactionEntity entity = findById(id);
        TypeCategory type = TypeCategory.toDescription(entity.getCategoryType());
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setPaymentDate(paymentDate);
        return paymentTransactionServiceFactory.execute(type, transaction);
    }

    @Override
    public TransactionEntity refund(Long id) {
        TransactionEntity entity = findById(id);
        TypeCategory type = TypeCategory.toDescription(entity.getCategoryType());
        return refundTransactionServiceFactory.execute(type, new Transaction(id));
    }

    @Override
    public void deleteById(Long id) {
        TransactionEntity entity = findById(id);
        TypeCategory type = TypeCategory.toDescription(entity.getCategoryType());
        deleteTransactionServiceFactory.batchDelete(false);
        deleteTransactionServiceFactory.execute(type, new Transaction(id));
    }

    @Override
    public void batchDelete(Long id) {
        TransactionEntity entity = findById(id);
        TypeCategory type = TypeCategory.toDescription(entity.getCategoryType());
        deleteTransactionServiceFactory.batchDelete(true);
        deleteTransactionServiceFactory.execute(type, new Transaction(id));
    }

    @Override
    public TransactionEntity batchUpdate(Long id, Transaction transaction) {
        TypeCategory type = TypeCategory.toDescription(transaction.getCategoryType());
        updateTransactionServiceFactory.batchUpdate(true);
        return updateTransactionServiceFactory.execute(type, transaction);
    }

    //    @Autowired
//    private TransactionRepository repository;
//
//    @Autowired
//    private TransferService transferService;
//
//    @Autowired
//    private AccountService accountService;
//
//    @Autowired
//    private CardService cardService;
//
//    @Autowired
//    private IncomeService incomeService;
//
//    @Autowired
//    private InstallmentService installmentService;
//
//    @Autowired
//    private FixedInstallmentService fixedInstallmentService;
//
//    public TransactionServiceImpl(TransactionRepository repository, AccountService accountService, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
//        super(repository, accountService, installmentService, fixedInstallmentService);
//    }
//
//    @Override
//    public TransactionEntity save(TransactionEntity entity) {
//        throw new NotificationException("Operação não suportada!", Severity.ERROR);
//    }
//
//    @Override
//    public TransactionEntity save(Transaction transaction) {
//        return switch (getTypeCategory(transaction.getCategoryType())) {
//            case INCOME, CORPORATE_CAPITAL -> save(transaction, TransactionType.CREDIT);
//            case TRANSFER -> transferService.betweenAccount(transaction);
//            case EXPENSE, INVESTMENT -> save(transaction, TransactionType.DEBIT);
//        };
//    }
//
//    @Override
//    public TransactionEntity pay(Long id, LocalDate paymentDate) {
//        TransactionEntity entity = findById(id);
//        entity.setPaymentDate(paymentDate);
//
//        switch (getTypeCategory(entity.getCategoryType())) {
//            case TRANSFER -> transferService.payOrRefund(entity, true);
//            case CORPORATE_CAPITAL, INCOME, EXPENSE, INVESTMENT -> pay(entity);
//            default -> throw new NotificationException("Transação não suportada!");
//        }
//        return entity;
//    }
//
//    @Override
//    public TransactionEntity refund(Long id) {
//        TransactionEntity entity = findById(id);
//        switch (getTypeCategory(entity.getCategoryType())) {
//            case TRANSFER -> transferService.payOrRefund(entity, false);
//            case CORPORATE_CAPITAL, INCOME, EXPENSE, INVESTMENT -> refund(entity);
//            default -> throw new NotificationException("Transação não suportada!");
//        }
//        return entity;
//    }
//
//    @Override
//    public BigDecimal accountBalace(final List<Long> listAccountId) {
//        List<Long> listId = new ArrayList<>(accountService.findAll()
//                .stream().map(AccountEntity::getId)
//                .toList());
//        return repository.accountBalance(listAccountId.isEmpty() ? listId : listAccountId);
//    }
//
//    @Override
//    public BigDecimal incomeBalance(final List<TransactionEntity> entities) {
//        return entities.stream().filter(t -> "INCOME".equals(t.getCategoryType()))
//                .map(t -> getValueOrZero(t.getValue()))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//    @Override
//    public BigDecimal expenseBalance(final List<TransactionEntity> entities) {
//        return entities.stream().filter(t -> "EXPENSE".equals(t.getCategoryType()))
//                .map(t -> getValueOrZero(t.getValue()))
//                .reduce(BigDecimal.ZERO, BigDecimal::add)
//                .abs();
//    }
//
//    @Override
//    public TransactionResponse update(Long id, Transaction transaction) {
//        return switch (getTypeCategory(transaction.getCategoryType())) {
//            case INCOME, CORPORATE_CAPITAL -> toMapper(update(transaction, TransactionType.CREDIT), TransactionResponse.class);
//            case TRANSFER -> toMapper(transferService.update(transaction), TransactionResponse.class);
//            case EXPENSE, INVESTMENT -> toMapper(incomeService.update(transaction), TransactionResponse.class);
//        };
//    }
//
//    @Override
//    public TransactionResponse searchById(Long id) {
//        TransactionEntity entity = findById(id);
//        entity.setValue(isDebit(entity) ? entity.getValue().abs() : entity.getValue());
//        return switch (getTypeCategory(entity.getCategoryType())) {
//            case INCOME, CORPORATE_CAPITAL, EXPENSE, INVESTMENT -> toMapper(entity, TransactionResponse.class);
//            case TRANSFER -> transferService.searchById(id);
//        };
//    }
//
//    @Override
//    public void deleteById(Long id, Boolean deleteOnlyThis) {
//        TransactionEntity entity = findById(id);
//        switch (getTypeCategory(entity.getCategoryType())) {
//            case TRANSFER -> transferService.delete(entity, deleteOnlyThis);
//            case CORPORATE_CAPITAL, EXPENSE, INCOME, INVESTMENT -> delete(entity, deleteOnlyThis);
//            default -> throw new NotificationException(entity.getCategoryType() + " :: Operação não suportada");
//        }
//    }
//
//    @Override
//    public List<TransactionEntity> findAll() {
//        return repository.findAll().stream()
//                .filter(this::isNotIgnoreTransaction)
//                .toList();
//    }
//
    @Override
    public Page<TransactionEntity> search(LocalDate dtInitial, LocalDate dtFinal, List<Long> accountListId, String text, Pageable pageable) {
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

//    @Override
//    public void batchUpdate(Transaction transaction) {
//        TransactionEntity entity = transaction.toEntity();
//        entity.setSignature(repository.findBySignature(entity.getId()));
//
//        if (TransactionType.DEBIT.name().equals(transaction.getTransactionType())) {
//            entity.setValue(invertSignal(entity.getValue()));
//        }
//
//        repository.save(entity);
//
//        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
//                .stream().filter(t -> t.getInstallment() > transaction.getInstallment())
//                .toList();
//
//        entities.forEach(t -> t.setValue(transaction.getValue()));
//
//        if ("FIXED".equals(transaction.getPaymentType())) {
//            int index = 99;
//            entities.subList(0, index).forEach(repository::save);
//            InsertTransactionScheduler.queue.add(entities.subList(index, entities.size()));
//        }
//
//        repository.saveAll(entities);
//    }
//
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
