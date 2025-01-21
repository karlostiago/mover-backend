package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Account;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.scheduler.InsertTransactionScheduler;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import com.ctsousa.mover.service.TransferService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class TransferServiceImpl extends BaseTransactionServiceImpl implements TransferService {


    private final InstallmentService installmentService;
    private final FixedInstallmentService fixedInstallmentService;

    public TransferServiceImpl(TransactionRepository repository, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, installmentService, fixedInstallmentService);
        this.installmentService = installmentService;
        this.fixedInstallmentService = fixedInstallmentService;
    }

    @Override
    public TransactionEntity save(Transaction transaction) {
        List<TransactionEntity> entities = new ArrayList<>();

        if (fixedInstallmentService.isFixed(transaction)) {
            entities = fixedInstallmentService.generated(transaction);
            InsertTransactionScheduler.queue.add(entities);
        }
        else if (installmentService.hasInstallment(transaction)) {
            entities = installmentService.generated(transaction);
            entities.forEach(repository::save);
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
        }

        updateAvailableBalance(transaction, null);

        return entities.stream().findFirst()
                .orElseThrow(() -> new NotificationException("Nenhuma transação encontrada."));
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        String signature = repository.findBySignature(transaction.getId());

        Account creditAccount = transaction.getDestinationAccount();

        Long entityId = repository.findBySignature(signature)
                .stream().map(TransactionEntity::getId)
                .filter(id -> !id.equals(transaction.getId()))
                .findFirst().orElseThrow();

        TransactionEntity debitEntity = transaction.toEntity();
        debitEntity.setSignature(signature);

        TransactionEntity creditEntity = transaction.toEntity();
        creditEntity.setId(entityId);
        creditEntity.setAccount(new AccountEntity(creditAccount.getId()));
        creditEntity.setTransactionType("CREDIT");
        creditEntity.setSignature(signature);
        creditEntity.setValue(invertSignal(debitEntity.getValue()));

        updateAvailableBalance(transaction, null);

        repository.save(debitEntity);
        repository.save(creditEntity);

        return creditEntity;
    }

    @Override
    protected void updateAvailableBalance(Transaction transaction, Long accountId) {
        if (isPaymentStatusChanged(transaction)) {
            List<AccountEntity> entities = new ArrayList<>(2);
            AccountEntity debitAccount = accountService.findById(transaction.getAccount().getId());
            AccountEntity creditAccount = accountService.findById(transaction.getDestinationAccount().getId());

            BigDecimal availableDebitBalance = debitAccount.getAvailableBalance();
            BigDecimal availableCreditBalance = creditAccount.getAvailableBalance();

            if (transaction.getPaid()) {
                availableCreditBalance = availableCreditBalance.add(transaction.getValue());
                availableDebitBalance = availableDebitBalance.subtract(transaction.getValue());
            }
            else {
                availableCreditBalance = availableCreditBalance.subtract(transaction.getValue());
                availableDebitBalance = availableDebitBalance.add(transaction.getValue());
            }

            debitAccount.setAvailableBalance(availableDebitBalance);
            creditAccount.setAvailableBalance(availableCreditBalance);

            entities.add(debitAccount);
            entities.add(creditAccount);

            entities.forEach(acc -> accountService.save(acc));
        }
    }

    @Override
    public TransactionEntity batchUpdate(Transaction transaction) {
        String signature = repository.findBySignature(transaction.getId());
        TransactionEntity entity = transaction.toEntity();
        entity.setSignature(signature);

        List<TransactionEntity> entities = repository.findBySignature(signature)
                .stream().filter(t -> t.getInstallment() >= entity.getInstallment())
                .toList();

        for (TransactionEntity entityUpdated : entities) {
            entityUpdated.setValue("CREDIT".equals(entityUpdated.getTransactionType()) ? invertSignal(entity.getValue()) : entity.getValue());
            entityUpdated.setDescription(entity.getDescription());
            entityUpdated.setSubcategory(entity.getSubcategory());
            entityUpdated.setAccount("CREDIT".equals(entityUpdated.getTransactionType())
                    ? new AccountEntity(transaction.getDestinationAccount().getId()) : new AccountEntity(transaction.getAccount().getId()));

            if (entity.getInstallment() == entityUpdated.getInstallment()) {
                entityUpdated.setPaid(transaction.getPaid());
                entityUpdated.setDueDate(entity.getDueDate());
                entityUpdated.setPaymentDate(entity.getPaymentDate());
            }
        }

        updateAvailableBalance(transaction, null);

        repository.saveAll(entities);

        return entities.stream().filter(t -> t.getId().equals(entity.getId()))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Lançamento não encontrado ID. " + entity.getId()));
    }

    @Override
    public void delete(Long id) {
        TransactionEntity entity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
                .stream().filter(t -> t.getInstallment() == entity.getInstallment())
                .toList();

        updatedAccount(entities);
    }

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
        TransactionEntity entity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
                .stream().filter(t -> t.getInstallment() == entity.getInstallment())
                .toList();

        for (TransactionEntity entityUpdated : entities) {
            entityUpdated.setPaymentDate(paymentDate);
            entityUpdated.setPaid(true);
            entityUpdated.setRefund(false);
            entityUpdated.setHour(LocalTime.now());
        }

        updateAvailableBalance(createTransaction(entities), null);

        repository.saveAll(entities);

        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Erro nao realizar o pagamento! Lançamento não encontrado."));
    }

    @Override
    public TransactionEntity refund(Long id) {
        TransactionEntity entity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
                .stream().filter(t -> t.getInstallment() == entity.getInstallment())
                .toList();

        for (TransactionEntity entityUpdated : entities) {
            entityUpdated.setPaymentDate(null);
            entityUpdated.setPaid(false);
            entityUpdated.setRefund(true);
            entityUpdated.setHour(LocalTime.now());
        }

        updateAvailableBalance(createTransaction(entities), null);

        repository.saveAll(entities);

        return entities.stream().filter(t -> t.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Erro nao realizar o estorno! Lançamento não encontrado."));
    }

    @Override
    public void batchDelete(Long id) {
        TransactionEntity entity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
                .stream().filter(t -> t.getInstallment() >= entity.getInstallment())
                .toList();
        updatedAccount(entities);
    }

    private void updatedAccount(List<TransactionEntity> entities) {
        Map<AccountEntity, BigDecimal> accumulatedBalance = entities.stream()
                .filter(TransactionEntity::getPaid)
                .collect(Collectors.groupingBy(
                        TransactionEntity::getAccount,
                        Collectors.reducing(BigDecimal.ZERO, TransactionEntity::getValue, BigDecimal::add)
                ));

        accumulatedBalance.forEach((account, balance) -> {
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                updateAccountBalance(account, account.getAvailableBalance().add(balance.abs()));
            } else {
                updateAccountBalance(account, account.getAvailableBalance().subtract(balance));
            }
        });

        repository.deleteAll(entities);
    }

    private Transaction createTransaction(List<TransactionEntity> entities) {
        Transaction transaction = new Transaction();
        for (TransactionEntity entity : entities) {
            if ("CREDIT".equals(entity.getTransactionType())) {
                transaction.setId(entity.getId());
                transaction.setPaid(entity.getPaid());
                transaction.setValue(entity.getValue().abs());
                transaction.setDestinationAccount(new Account(entity.getAccount().getId()));
            }
            else {
                transaction.setAccount(new Account(entity.getAccount().getId()));
            }
        }
        return transaction;
    }
}
