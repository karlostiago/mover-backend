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

        for (TransactionEntity entity : entities) {
            if (entity.getPaid()) continue;
            entity.setPaid(true);
            entity.setPaymentDate(entity.getPaymentDate() == null ? entity.getDueDate() : entity.getPaymentDate());
            updateBalance(entity.getAccount(), entity.getValue());
            repository.save(entity);
        }
    }

    public void refund(String signature) {
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            if (!entity.getPaid()) continue;
            entity.setPaid(false);
            entity.setRefund(true);
            entity.setPaymentDate(null);
            updateBalance(entity.getAccount(), invertSignal(entity.getValue()));
            repository.save(entity);
        }
    }

    public void delete(TransactionEntity entity) {
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
        for (TransactionEntity transaction : entities) {
            if (entity.getPaid()) {
                updateBalance(transaction.getAccount(), invertSignal(transaction.getValue()));
            }
            super.deleteById(transaction.getId());
        }
    }

    public TransactionEntity save(Transaction transaction, TransactionType transactionType) {

        boolean hasInstallment = installmentService.hasInstallment(transaction);
        if (hasInstallment) {
            List<TransactionEntity> entities = installmentService.generated(transaction);
            return entities.stream().findFirst()
                    .orElseThrow(() -> new NotificationException("Nenhuma transação encontrada."));
        } else {
            TransactionEntity entity = transaction.toEntity();
            entity.setTransactionType(transactionType.name());
            entity.setValue(TransactionType.DEBIT.equals(transactionType) ? invertSignal(transaction.getValue()) : transaction.getValue());

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

    public void updateBalance(AccountEntity account, BigDecimal value) {
        BigDecimal balance = account.getAvailableBalance().add(value);
        account.setAvailableBalance(balance);
        accountService.save(account);
    }

    protected Boolean isDebit(TransactionEntity entity) {
        return entity.getTransactionType().equals("DEBIT");
    }
}
