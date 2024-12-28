package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.Transform;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import com.ctsousa.mover.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class TransferServiceImpl extends BaseTransactionServiceImpl implements TransferService {

    private final String CREDIT_LABEL = "CREDIT_";
    private final String DEBIT_LABEL = "DEBIT_";

    @Autowired
    private final AccountService accountService;

    private final InstallmentService installmentService;

    private final FixedInstallmentService fixedInstallmentService;

    public TransferServiceImpl(TransactionRepository repository, AccountService accountService, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, accountService, installmentService, fixedInstallmentService);
        this.accountService = accountService;
        this.installmentService = installmentService;
        this.fixedInstallmentService = fixedInstallmentService;
    }

    @Override
    public TransactionEntity betweenAccount(Transaction transaction) {
        List<TransactionEntity> entities = new ArrayList<>();

        if (fixedInstallmentService.isFixed(transaction)) {
            entities = fixedInstallmentService.generated(transaction);
        }
        else if (installmentService.hasInstallment(transaction)) {
            entities = installmentService.generated(transaction);
        }
        else {
            AccountEntity creditAccount = new AccountEntity(transaction.getDestinationAccount().getId());
            AccountEntity debitAccount = new AccountEntity((transaction.getAccount().getId()));

            TransactionEntity creditEntity = transaction.toEntity();
            creditEntity.setTransactionType(TransactionType.CREDIT.name());
            creditEntity.setAccount(creditAccount);
            entities.add(creditEntity);

            TransactionEntity debitEntity = transaction.toEntity();
            debitEntity.setTransactionType(TransactionType.DEBIT.name());
            debitEntity.setAccount(debitAccount);
            debitEntity.setValue(invertSignal(debitEntity.getValue()));
            debitEntity.setSignature(creditEntity.getSignature());
            entities.add(debitEntity);
        }

        entities.forEach(this::saveAndUpdateBalance);

        return entities.stream().findFirst()
                .orElseThrow(() -> new NotificationException("Nenhuma transação encontrada."));
    }

    @Override
    public void delete(TransactionEntity entity, Boolean deleteOnlyThis) {
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
        List<TransactionEntity> entitiesToDelete = new ArrayList<>(entities.size());

        Map<String, TransactionEntity> mapTransactions = getTransactionMap(entities);

        TransactionEntity credit = mapTransactions.get(CREDIT_LABEL + entity.getInstallment());
        entitiesToDelete.add(credit);

        TransactionEntity debit = mapTransactions.get(DEBIT_LABEL + entity.getInstallment());
        entitiesToDelete.add(debit);

        if (Boolean.FALSE.equals(deleteOnlyThis)) {
            for (TransactionEntity e : entities) {
                if (entitiesToDelete.contains(e)) continue;
                if (e.getInstallment() > entity.getInstallment()) {
                    credit = mapTransactions.get(CREDIT_LABEL + e.getInstallment());
                    debit = mapTransactions.get(DEBIT_LABEL + e.getInstallment());
                    entitiesToDelete.add(credit);
                    entitiesToDelete.add(debit);
                }
            }
        }

        entitiesToDelete.forEach(this::deleteAndUpdateBalance);
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        TransactionEntity entity = findById(transaction.getId());
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
        AccountEntity creditAccount = accountService.findById(transaction.getDestinationAccount().getId());
        AccountEntity debitAccount = accountService.findById(transaction.getAccount().getId());

        for (TransactionEntity e : entities) {
            updateTransaction(e, transaction, debitAccount, creditAccount);
            updateBalance(entity, e, transaction.getValue());
        }

        return entity;
    }

    @Override
    public TransactionResponse searchById(Long id) {
        TransactionEntity transactionEntity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(transactionEntity.getSignature());
        TransactionResponse response = Transform.toMapper(transactionEntity, TransactionResponse.class);

        for (TransactionEntity entity : entities) {
            if (isDebit(entity)) {
                response.setValue(invertSignal(entity.getValue()));
                response.setAccountId(entity.getAccount().getId());
            } else {
                response.setDestinationAccountId(entity.getAccount().getId());
            }
        }

        return response;
    }

    @Override
    public void payOrRefund(TransactionEntity entity, Boolean pay) {
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
        Map<String, TransactionEntity> mapTransactions = getTransactionMap(entities);
        TransactionEntity credit = mapTransactions.get(CREDIT_LABEL + entity.getInstallment());
        TransactionEntity debit = mapTransactions.get(DEBIT_LABEL + entity.getInstallment());
        if (Boolean.TRUE.equals(pay)) {
            super.pay(credit);
            super.pay(debit);
        } else {
            super.refund(credit);
            super.refund(debit);
        }
    }

    private Map<String, TransactionEntity> getTransactionMap(List<TransactionEntity> entities) {
        Map<String, TransactionEntity> transactionMap = new HashMap<>();
        for (TransactionEntity e : entities) {
            if ("CREDIT".equals(e.getTransactionType())) {
                transactionMap.put(TransactionType.CREDIT + "_" + e.getInstallment(), e);
            } else {
                transactionMap.put(TransactionType.DEBIT + "_" + e.getInstallment(), e);
            }
        }
        return transactionMap;
    }

    private void updateBalance(TransactionEntity originalTransaction, TransactionEntity updatedTransaction, BigDecimal balance) {
        boolean wasPaid = originalTransaction.getPaid();
        boolean isNowPaid = updatedTransaction.getPaid();
        if (!wasPaid && isNowPaid) {
            updateBalance(updatedTransaction.getAccount(), updatedTransaction.getValue());
        } else if (wasPaid && !isNowPaid) {
            var value = isDebit(updatedTransaction) ? balance : invertSignal(balance);
            updateBalance(updatedTransaction.getAccount(), value);
        }
    }

    private void updateTransaction(TransactionEntity entity, Transaction transaction, AccountEntity debitAccount, AccountEntity creditAccount) {
        entity.setDescription(transaction.getDescription());
        entity.setDueDate(transaction.getDueDate());
        entity.setPaymentDate(transaction.getPaymentDate());
        entity.setPaid(transaction.getPaid());

        if (isDebit(entity)) {
            entity.setAccount(debitAccount);
            entity.setValue(invertSignal(transaction.getValue()));
        } else {
            entity.setAccount(creditAccount);
            entity.setValue(transaction.getValue());
        }

        repository.save(entity);
    }
}
