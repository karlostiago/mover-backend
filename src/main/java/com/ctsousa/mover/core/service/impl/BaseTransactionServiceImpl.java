package com.ctsousa.mover.core.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.scheduler.InsertTransactionScheduler;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

public class BaseTransactionServiceImpl extends BaseServiceImpl<TransactionEntity, Long> {

    private final AccountService accountService;

    protected final TransactionRepository repository;

    private final InstallmentService installmentService;

    private final FixedInstallmentService fixedInstallmentService;

    public BaseTransactionServiceImpl(TransactionRepository repository, AccountService accountService, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository);
        this.repository = repository;
        this.accountService = accountService;
        this.installmentService = installmentService;
        this.fixedInstallmentService = fixedInstallmentService;
    }

    public void pay(final TransactionEntity entity) {
        if (entity.getPaid()) return;
        entity.setPaid(true);
        entity.setRefund(false);
        updateBalance(entity.getAccount(), entity.getValue());
        repository.save(entity);
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

        validatedSave(transaction);

        boolean hasInstallment = installmentService.hasInstallment(transaction);
        boolean isFixed = fixedInstallmentService.isFixed(transaction);
        transaction.setValue(TransactionType.DEBIT.equals(transactionType) ? invertSignal(transaction.getValue()) : transaction.getValue());
        transaction.setTransactionType(transactionType.name());

        List<TransactionEntity> entities;

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
            TransactionEntity entity = transaction.toEntity();
            entity.setTransactionType(transactionType.name());
            repository.save(entity);
            return entity;
        }

        return entities.stream().findFirst()
                .orElseThrow(() -> new NotificationException("Ocorreu um erro ao salvar o lançamento."));
    }

    public TransactionEntity update(Transaction transaction, TransactionType transactionType) {
        TransactionEntity originalTransaction = findById(transaction.getId());
        String signature = originalTransaction.getSignature();

        boolean wasPaid = originalTransaction.getPaid();
        boolean isNowPaid = transaction.getPaid();

        TransactionEntity entity = transaction.toEntity();
        entity.setTransactionType(transactionType.name());
        entity.setAccount(new AccountEntity(transaction.getAccount().getId()));
        entity.setSignature(signature);
        entity.setValue(TransactionType.DEBIT.equals(transactionType) ? invertSignal(transaction.getValue()) : transaction.getValue());

        if ("IN_INSTALLMENTS".equals(originalTransaction.getPaymentType())) {
            entity.setInstallment(originalTransaction.getInstallment());
            entity.setFrequency(originalTransaction.getFrequency());
            entity.setPaymentType(originalTransaction.getPaymentType());
        }

        if ("FIXED".equals(originalTransaction.getPaymentType())) {
            entity.setInstallment(originalTransaction.getInstallment());
            entity.setFrequency(originalTransaction.getFrequency());
            entity.setPaymentType(originalTransaction.getPaymentType());
            entity.setPredicted(Boolean.TRUE);
        }

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

    protected void saveAndUpdateBalance(TransactionEntity entity) {
        if(entity.getPaid()) {
            updateBalance(entity.getAccount(), entity.getValue());
        }
        super.save(entity);
    }

    public static LocalDate calculateDueDate(LocalDate dueDate, String frequency, long installment) {
        try {
            final long FIRST_INSTALLMENT = 0;
            if (installment == FIRST_INSTALLMENT) return dueDate;
            PaymentFrequency paymentFrequency = PaymentFrequency.toDescription(frequency);
            return dueDate.plusDays(paymentFrequency.days() * installment);
        } catch (NotificationException e) {
            throw new NotificationException("Não há suporte para calcular a data de vencimento para a frequência informada.");
        }
    }

    private void validatedSave(Transaction transaction) {
        if ("FIXED".equals(transaction.getPaymentType()) && transaction.getFrequency().isEmpty()) {
            throw new NotificationException("Erro ao salvar, para lançamento fixo é necessário informar a frenquência.");
        }
        if ("IN_INSTALLMENTS".equals(transaction.getPaymentType()) && (transaction.getFrequency().isEmpty() || Integer.valueOf(0).equals(transaction.getInstallment()))) {
            throw new NotificationException("Erro ao salvar, para lançamento parcelado é necessário informar a frenquência e o número de parcelas.");
        }
    }
}
