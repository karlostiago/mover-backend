package com.ctsousa.mover.core.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.InstallmentService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

public class BaseTransactionServiceImpl extends BaseServiceImpl<TransactionEntity, Long> {

    private final AccountService accountService;

    protected final TransactionRepository repository;

    private final InstallmentService installmentService;

    public BaseTransactionServiceImpl(TransactionRepository repository, AccountService accountService, InstallmentService installmentService) {
        super(repository);
        this.repository = repository;
        this.accountService = accountService;
        this.installmentService = installmentService;
    }

    public void pay(final String signature) {
        List<TransactionEntity> entities = repository.findBySignature(signature);
        entities.forEach(this::pay);
    }

    public void pay(final TransactionEntity entity) {
        if (entity.getPaid()) return;
        entity.setPaid(true);
        entity.setPaymentDate(entity.getPaymentDate() == null ? entity.getDueDate() : entity.getPaymentDate());
        updateBalance(entity.getAccount(), entity.getValue());
        repository.save(entity);
    }

    public void refund(String signature) {
        List<TransactionEntity> entities = repository.findBySignature(signature);
        entities.forEach(this::refund);
    }

    public void refund(TransactionEntity entity) {
        if (!entity.getPaid()) return;
        entity.setPaid(false);
        entity.setRefund(true);
        entity.setPaymentDate(null);
        updateBalance(entity.getAccount(), invertSignal(entity.getValue()));
        repository.save(entity);
    }

    public void delete(TransactionEntity entity, Boolean deleteOnlyThis) {
        if (Boolean.TRUE.equals(deleteOnlyThis)) {
            deleteAndUpdateBalance(entity);
        } else {
            List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
            List<TransactionEntity> entitiesToDelete = new ArrayList<>(entities.size());
            entitiesToDelete.add(entity);
            entitiesToDelete.addAll(entities.stream().filter(e -> e.getInstallment() > entity.getInstallment()).toList());
            for (TransactionEntity e : entitiesToDelete) {
                deleteAndUpdateBalance(e);
            }
        }
    }

    public void delete(String signature, Boolean deleteOnlyThis) {
        List<TransactionEntity> entities = repository.findBySignature(signature);
        entities.forEach(entity -> delete(entity, deleteOnlyThis));
    }

    public TransactionEntity save(Transaction transaction, TransactionType transactionType) {

        boolean hasInstallment = installmentService.hasInstallment(transaction);
        transaction.setValue(TransactionType.DEBIT.equals(transactionType) ? invertSignal(transaction.getValue()) : transaction.getValue());
        transaction.setTransactionType(transactionType.name());

        if (hasInstallment) {
            List<TransactionEntity> entities = installmentService.generated(transaction);
            entities.forEach(repository::save);
            return entities.stream().findFirst()
                    .orElseThrow(() -> new NotificationException("Nenhuma transação encontrada."));
        } else {
            TransactionEntity entity = transaction.toEntity();
            entity.setTransactionType(transactionType.name());

            if (entity.getPaid()) {
                updateBalance(accountService.findById(entity.getAccount().getId()), entity.getValue());
            }

            return repository.save(entity);
        }
    }

    public TransactionEntity update(Transaction transaction, TransactionType transactionType) {
        TransactionEntity originalTransaction = findById(transaction.getId());
        String signature = originalTransaction.getSignature();

        boolean wasPaid = originalTransaction.getPaid();
        boolean isNowPaid = transaction.getPaid();

        TransactionEntity entity = transaction.toEntity();
        entity.setTransactionType(transactionType.name());
        entity.setAccount(accountService.findById(transaction.getAccount().getId()));
        entity.setSignature(signature);
        entity.setValue(TransactionType.DEBIT.equals(transactionType) ? invertSignal(transaction.getValue()) : transaction.getValue());

        if (wasPaid && !isNowPaid) {
            updateBalance(entity.getAccount(), invertSignal(transaction.getValue()));
        } else if (!wasPaid && isNowPaid) {
            updateBalance(entity.getAccount(), transaction.getValue());
        }

        return repository.save(entity);
    }

    public void updateBalance(final AccountEntity account, BigDecimal value) {
        AccountEntity accountFound = accountService.findById(account.getId());
        BigDecimal balance = accountFound.getAvailableBalance().add(value);
        accountFound.setAvailableBalance(balance);
        accountService.save(accountFound);
    }

    protected Boolean isDebit(TransactionEntity entity) {
        return entity.getTransactionType().equals("DEBIT");
    }

    protected void deleteAndUpdateBalance(TransactionEntity entity) {
        if(entity.getPaid()) {
            updateBalance(entity.getAccount(), invertSignal(entity.getValue()));
        }
        super.deleteById(entity.getId());
    }
}
