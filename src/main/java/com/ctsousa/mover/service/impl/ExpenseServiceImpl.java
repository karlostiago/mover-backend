package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.core.util.NumberUtil;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.scheduler.InsertTransactionScheduler;
import com.ctsousa.mover.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class ExpenseServiceImpl extends BaseTransactionServiceImpl implements ExpenseService {

    @Autowired
    private final AccountService accountService;

    private final InstallmentService installmentService;
    private final FixedInstallmentService fixedInstallmentService;

    public ExpenseServiceImpl(TransactionRepository repository, AccountService accountService, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, accountService, null, null);
        this.accountService = accountService;
        this.installmentService = installmentService;
        this.fixedInstallmentService = fixedInstallmentService;
    }

    @Override
    public TransactionEntity save(Transaction transaction) {
        validatedSave(transaction);
        TransactionEntity entity = transaction.toEntity();
        transaction.setTransactionType(entity.getTransactionType());

        boolean hasInstallment = installmentService.hasInstallment(transaction);
        boolean isFixed = fixedInstallmentService.isFixed(transaction);

        List<TransactionEntity> entities = new ArrayList<>();

        if (isFixed) {
            int toIndex = 100;
            entities = fixedInstallmentService.generated(transaction);
            entities.subList(0, toIndex).forEach(repository::save);
            InsertTransactionScheduler.queue.add(entities.subList(toIndex, entities.size()));
        }
        else if (hasInstallment) {
            entities = installmentService.generated(transaction);
            entities.forEach(repository::save);
        }
        else {
            TransactionEntity entitySaved = repository.save(entity);
            entities.add(entitySaved);
        }

        return entities.stream().findFirst()
                .orElseThrow(() -> new NotificationException("Erro ao salvar lançamento."));
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        String signature = repository.findBySignature(transaction.getId());
        TransactionEntity entity = transaction.toEntity();
        entity.setSignature(signature);
        return repository.save(entity);
    }

    @Override
    public TransactionEntity batchUpdate(Transaction transaction) {
        return null;
    }

    @Override
    public TransactionEntity filterById(Long id) {
        TransactionEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotificationException("Transação não encontrada"));

        if ("DEBIT".equals(entity.getTransactionType())) {
            entity.setValue(invertSignal(entity.getValue()));
        }

        return entity;
    }

    @Override
    public TransactionEntity payment(Long id, LocalDate paymentDate) {
        TransactionEntity entity = findById(id);

        if (entity.getPaid()) {
            return entity;
        }

        entity.setPaymentDate(paymentDate);
        entity.setPaid(true);
        entity.setRefund(false);
        entity.setHour(LocalTime.now());
        return repository.save(entity);
    }

    @Override
    public TransactionEntity refund(Long id) {
        TransactionEntity entity = findById(id);
        entity.setPaymentDate(null);
        entity.setPaid(false);
        entity.setRefund(true);
        entity.setHour(LocalTime.now());
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void batchDelete(Long id) {
        TransactionEntity entity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
                .stream().filter(t -> t.getInstallment() >= entity.getInstallment())
                .toList();
        repository.deleteAll(entities);
    }
}
