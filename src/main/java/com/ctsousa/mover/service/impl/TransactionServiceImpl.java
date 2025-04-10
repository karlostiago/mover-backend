package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.factory.*;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.parseMonetary;
import static com.ctsousa.mover.core.util.StringUtil.toUppercase;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
public class TransactionServiceImpl implements TransactionService {

    private final CreateTransactionServiceFactory createTransactionServiceFactory;

    private final UpdateTransactionServiceFactory updateTransactionServiceFactory;

    private final FilterByIdTransactionServiceFactory filterTransactionServiceFactory;

    private final PaymentTransactionServiceFactory paymentTransactionServiceFactory;

    private final RefundTransactionServiceFactory refundTransactionServiceFactory;

    private final DeleteTransactionServiceFactory deleteTransactionServiceFactory;

    private final ScheduleTransactionServiceFactory scheduleTransactionServiceFactory;

    private final UndoSchedulingTransactionServiceFactory undoSchedulingTransactionServiceFactory;

    private final TransactionRepository repository;

    public TransactionServiceImpl(CreateTransactionServiceFactory createTransactionServiceFactory, UpdateTransactionServiceFactory updateTransactionServiceFactory, FilterByIdTransactionServiceFactory filterTransactionServiceFactory, PaymentTransactionServiceFactory paymentTransactionServiceFactory, RefundTransactionServiceFactory refundTransactionServiceFactory, DeleteTransactionServiceFactory deleteTransactionServiceFactory, ScheduleTransactionServiceFactory scheduleTransactionServiceFactory, UndoSchedulingTransactionServiceFactory undoSchedulingTransactionServiceFactory, TransactionRepository repository) {
        this.createTransactionServiceFactory = createTransactionServiceFactory;
        this.updateTransactionServiceFactory = updateTransactionServiceFactory;
        this.filterTransactionServiceFactory = filterTransactionServiceFactory;
        this.paymentTransactionServiceFactory = paymentTransactionServiceFactory;
        this.refundTransactionServiceFactory = refundTransactionServiceFactory;
        this.deleteTransactionServiceFactory = deleteTransactionServiceFactory;
        this.scheduleTransactionServiceFactory = scheduleTransactionServiceFactory;
        this.undoSchedulingTransactionServiceFactory = undoSchedulingTransactionServiceFactory;
        this.repository = repository;
    }

    @Override
    public TransactionEntity save(TransactionEntity entity) {
        throw new NotificationException("Método não suportado");
    }

    @Override
    public TransactionEntity findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotificationException("Lançamento não encontrado."));
    }

    @Override
    public List<TransactionEntity> findAll() {
        throw new NotificationException("Método não suportado");
    }

    @Override
    public TransactionEntity update(TransactionEntity entity) {
        throw new NotificationException("Método não suportado");
    }

    @Override
    public void existsById(Long id) {
        findById(id);
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
    public TransactionEntity schedule(Long id) {
        TransactionEntity entity = findById(id);
        TypeCategory type = TypeCategory.toDescription(entity.getCategoryType());
        return scheduleTransactionServiceFactory.execute(type, new Transaction(id));
    }

    @Override
    public TransactionEntity undoScheduling(Long id) {
        TransactionEntity entity = findById(id);
        TypeCategory type = TypeCategory.toDescription(entity.getCategoryType());
        return undoSchedulingTransactionServiceFactory.execute(type, new Transaction(id));
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

    @Override
    public Page<TransactionEntity> search(Transaction.Filter filter, Pageable pageable) {
        return search(filter.getDtInitial(), filter.getDtFinal(), filter.getAccountsId(), filter.getText(), pageable);
    }

    private Page<TransactionEntity> search(LocalDate dtInitial, LocalDate dtFinal, List<Long> accountListId, String text, Pageable pageable) {
        var value = parseMonetary(text);
        text = value != null ? null : toUppercase(text);
        Page<TransactionEntity> page;
        List<TransactionEntity> entities;
        if (hasAccountAndText(accountListId, text)) {
            page = repository.findByPeriodAndAccountAndDescription(dtInitial, dtFinal, accountListId, text, pageable);
        }
        else if (hasAccountAndValue(accountListId, value)) {
            page = repository.findByPeriodAndAccountAndValue(dtInitial, dtFinal, accountListId, value, pageable);
        }
        else if (hasValueAndNotAccount(accountListId, value)) {
            page = repository.findByPeriodAndValue(dtInitial, dtFinal, value, pageable);
        }
        else if (hasTextAndNotAccount(accountListId, text)) {
            page = repository.findByPeriodAndDescription(dtInitial, dtFinal, text, pageable);
        }
        else if (hasAccount(accountListId)) {
            page = repository.findByPeriodAndAccount(dtInitial, dtFinal, accountListId, pageable);
        }
        else {
            page = repository.findByPeriod(dtInitial, dtFinal, pageable);
        }

        entities = new ArrayList<>(page.stream().toList());

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
}
