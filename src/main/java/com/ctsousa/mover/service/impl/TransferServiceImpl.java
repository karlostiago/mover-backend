package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.mapper.Transform;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.core.util.NumberUtil;
import com.ctsousa.mover.domain.Account;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.response.TransactionResponse;
import com.ctsousa.mover.scheduler.InsertTransactionScheduler;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import com.ctsousa.mover.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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
    public void delete(TransactionEntity entity, Boolean deleteOnlyThis) {
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
        List<TransactionEntity> entitiesToDelete = new ArrayList<>(entities.size());

        Map<String, TransactionEntity> mapTransactions = buildTransactionsCreditAndDebit(entities);

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
    public TransactionEntity save(Transaction transaction) {
        List<TransactionEntity> entities = new ArrayList<>();
        boolean hasBeenSaved = false;

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
            creditEntity.setValue(transaction.getValue().abs());
            entities.add(creditEntity);

            TransactionEntity debitEntity = transaction.toEntity();
            debitEntity.setTransactionType(TransactionType.DEBIT.name());
            debitEntity.setAccount(debitAccount);
            debitEntity.setSignature(creditEntity.getSignature());
            entities.add(debitEntity);

            repository.saveAll(entities);
            hasBeenSaved = true;
        }

        if (!hasBeenSaved) {
            InsertTransactionScheduler.queue.add(entities);
        }

        return entities.stream().findFirst()
                .orElseThrow(() -> new NotificationException("Nenhuma transação encontrada."));
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        String signature = repository.findBySignature(transaction.getId());

        Account account = transaction.getDestinationAccount();

        Long entityId = repository.findBySignature(signature)
                .stream().map(TransactionEntity::getId)
                .filter(id -> !id.equals(transaction.getId()))
                .findFirst()
                .orElseThrow();

        TransactionEntity entity = transaction.toEntity();
        entity.setSignature(signature);
        repository.save(entity);

        entity.setId(entityId);
        entity.setAccount(new AccountEntity(account.getId()));
        entity.setTransactionType("CREDIT");
        entity.setValue(invertSignal(entity.getValue()));

        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        String sginature = repository.findBySignature(id);
        List<TransactionEntity> entities = repository.findBySignature(sginature);
        repository.deleteAll(entities);
    }

    //    @Override
//    public TransactionEntity update(Transaction transaction) {
//        TransactionEntity entity = findById(transaction.getId());
//
//        Map<TransactionType, TransactionEntity> transactions = getTransactions(entity.getSignature(), entity.getInstallment());
//        List<TransactionEntity> entitiesToUpdate = new ArrayList<>();
//        entitiesToUpdate.add(transactions.get(TransactionType.DEBIT));
//        entitiesToUpdate.add(transactions.get(TransactionType.CREDIT));
//
//        AccountEntity creditAccount = accountService.findById(transaction.getDestinationAccount().getId());
//        AccountEntity debitAccount = accountService.findById(transaction.getAccount().getId());
//
//        for (TransactionEntity e : entitiesToUpdate) {
//            updateTransaction(e, transaction, debitAccount, creditAccount);
//            updateBalance(entity, e, transaction.getValue());
//        }
//
//        return entity;
//    }

    @Override
    public TransactionEntity filterById(Long id) {
        String signature = repository.findBySignature(id);

        if (signature == null) {
            throw new NotificationException("Nenhuma transação não encontrada.");
        }

        List<TransactionEntity> entities = repository.findBySignature(signature);

        TransactionEntity entity = entities.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Nenhuma transação não encontrada."));

        AccountEntity account = entities.stream()
                .filter(t -> !t.getId().equals(id))
                .map(TransactionEntity::getAccount)
                .findFirst()
                .orElseThrow(() -> new NotificationException("Nenhuma transação não encontrada."));

        entity.setDestinationAccount(account);

        if ("DEBIT".equals(entity.getTransactionType())) {
            entity.setValue(invertSignal(entity.getValue()));
        }

        return entity;
    }

    @Override
    public TransactionEntity payment(Long id, LocalDate paymentDate) {
        String signature = repository.findBySignature(id);
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            entity.setPaymentDate(paymentDate);
            entity.setPaid(true);
            entity.setRefund(false);
            entity.setHour(LocalTime.now());
            repository.save(entity);
        }

        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Erro nao realizar o pagamento! Lançamento não encontrado."));
    }

    @Override
    public TransactionEntity refund(Long id) {
        String signature = repository.findBySignature(id);
        List<TransactionEntity> entities = repository.findBySignature(signature);

        for (TransactionEntity entity : entities) {
            entity.setPaymentDate(null);
            entity.setPaid(false);
            entity.setRefund(true);
            entity.setHour(LocalTime.now());
            repository.save(entity);
        }

        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Erro nao realizar o estorno! Lançamento não encontrado."));
    }

    @Deprecated
    @Override
    public TransactionResponse searchById(Long id) {
        TransactionEntity entity = findById(id);
        Map<TransactionType, TransactionEntity> transactions = getTransactions(entity.getSignature(), entity.getInstallment());
        TransactionEntity creditTransaction = transactions.get(TransactionType.CREDIT);
        TransactionResponse response = Transform.toMapper(entity, TransactionResponse.class);
        response.setValue(invertSignal(entity.getValue()));
        response.setDestinationAccountId(creditTransaction.getAccount().getId());
        return response;
    }

    @Override
    public void payOrRefund(TransactionEntity entity, Boolean pay) {
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature());
        Map<String, TransactionEntity> mapTransactions = buildTransactionsCreditAndDebit(entities);
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

    private Map<TransactionType, TransactionEntity> getTransactions(final String signature, final int installment) {
        List<TransactionEntity> transactions = repository.findBySignature(signature);
        Map<String, TransactionEntity> mapTransactions = buildTransactionsCreditAndDebit(transactions);
        Map<TransactionType, TransactionEntity> map = new HashMap<>();
        map.put(TransactionType.CREDIT,  mapTransactions.get(CREDIT_LABEL + installment));
        map.put(TransactionType.DEBIT,  mapTransactions.get(DEBIT_LABEL + installment));
        return map;
    }

    private Map<String, TransactionEntity> buildTransactionsCreditAndDebit(List<TransactionEntity> entities) {
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
        entity.setHour(LocalTime.now());

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
