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
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import com.ctsousa.mover.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
        super(repository, installmentService, fixedInstallmentService);
        this.accountService = accountService;
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

        repository.saveAll(entities);

        return entities.stream().filter(t -> t.getId().equals(entity.getId()))
                .findFirst()
                .orElseThrow(() -> new NotificationException("Lançamento não encontrado ID. " + entity.getId()));
    }

    @Override
    public void delete(Long id) {
        String sginature = repository.findBySignature(id);
        List<TransactionEntity> entities = repository.findBySignature(sginature);
        repository.deleteAll(entities);
    }

    @Override
    public void batchDelete(Long id) {
        TransactionEntity entity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
                .stream().filter(t -> t.getInstallment() >= entity.getInstallment())
                .toList();
        repository.deleteAll(entities);
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
}
