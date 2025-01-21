package com.ctsousa.mover.core.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.scheduler.InsertTransactionScheduler;
import com.ctsousa.mover.service.AccountService;
import com.ctsousa.mover.service.FixedInstallmentService;
import com.ctsousa.mover.service.InstallmentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

public class BaseTransactionServiceImpl extends BaseServiceImpl<TransactionEntity, Long> {

    @Autowired
    protected TransactionRepository repository;

    @Autowired
    protected AccountService accountService;

    private final InstallmentService installmentService;
    private final FixedInstallmentService fixedInstallmentService;

    public BaseTransactionServiceImpl(TransactionRepository repository, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository);
        this.installmentService = installmentService;
        this.fixedInstallmentService = fixedInstallmentService;
    }

    public TransactionEntity save(Transaction transaction) {
        validatedSave(transaction);
        TransactionEntity entity = transaction.toEntity();
        transaction.setTransactionType(entity.getTransactionType());

        boolean hasInstallment = installmentService.hasInstallment(transaction);
        boolean isFixed = fixedInstallmentService.isFixed(transaction);

        List<TransactionEntity> entities = new ArrayList<>();

        if (isFixed) {
            entities = fixedInstallmentService.generated(transaction);
            InsertTransactionScheduler.queue.add(entities);
        }
        else if (hasInstallment) {
            entities = installmentService.generated(transaction);
            entities.forEach(repository::save);
        }
        else {
            TransactionEntity entitySaved = repository.save(entity);
            entities.add(entitySaved);
        }

        updateAvailableBalance(transaction, transaction.getAccount().getId());

        return entities.stream().findFirst()
                .orElseThrow(() -> new NotificationException("Erro ao salvar lançamento."));
    }

    public TransactionEntity update(Transaction transaction) {
        String signature = repository.findBySignature(transaction.getId());
        TransactionEntity entity = transaction.toEntity();
        entity.setSignature(signature);
        updateAvailableBalance(transaction, entity.getAccount().getId());
        return repository.save(entity);
    }

    public TransactionEntity batchUpdate(Transaction transaction) {
        TransactionEntity entityFound = findById(transaction.getId());
        List<TransactionEntity> entities = repository.findBySignature(entityFound.getSignature())
                .stream().filter(t -> t.getInstallment() >= entityFound.getInstallment())
                .toList();

        TransactionEntity entity = transaction.toEntity();

        for (TransactionEntity entityUpdate : entities) {
            entityUpdate.setDescription(entity.getDescription());
            entityUpdate.setSubcategory(entity.getSubcategory());
            entityUpdate.setVehicle(entity.getVehicle());
            entityUpdate.setContract(entity.getContract());
            entityUpdate.setAccount(entity.getAccount());
            entityUpdate.setPartner(entity.getPartner());
            entityUpdate.setCard(entity.getCard());
            entityUpdate.setValue(entity.getValue());

            if (entityUpdate.getInstallment() == transaction.getInstallment()) {
                entityUpdate.setDueDate(transaction.toEntity().getDueDate());
                entityUpdate.setPaymentDate(transaction.toEntity().getPaymentDate());
                entityUpdate.setPaid(entity.getPaid());
            }
        }

        InsertTransactionScheduler.queue.add(entities);

        updateAvailableBalance(transaction, entity.getAccount().getId());

        return entities.stream().findFirst()
                .orElseThrow(() -> new NotificationException("Erro ao atualizar em lote de lançamentos."));
    }

    public TransactionEntity filterById(Long id) {
        TransactionEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotificationException("Transação não encontrada"));

        if ("DEBIT".equals(entity.getTransactionType())) {
            entity.setValue(invertSignal(entity.getValue()));
        }

        return entity;
    }

    public TransactionEntity payment(Long id, LocalDate paymentDate) {
        TransactionEntity entity = findById(id);

        if (entity.getPaid()) {
            return entity;
        }

        entity.setPaymentDate(paymentDate);
        entity.setPaid(true);
        entity.setRefund(false);
        entity.setHour(LocalTime.now());

        repository.save(entity);

        BigDecimal availableBalance = entity.getAccount().getAvailableBalance()
                .add(entity.getValue());

        updateAccountBalance(entity.getAccount(), availableBalance);

        return entity;
    }

    public TransactionEntity refund(Long id) {
        TransactionEntity entity = findById(id);
        entity.setPaymentDate(null);
        entity.setPaid(false);
        entity.setRefund(true);
        entity.setHour(LocalTime.now());

        repository.save(entity);

        BigDecimal availableBalance = entity.getAccount().getAvailableBalance()
                        .subtract(entity.getValue());

        updateAccountBalance(entity.getAccount(), availableBalance);

        return entity;
    }

    public void delete(Long id) {
        TransactionEntity entity = findById(id);

        if (entity.getPaid()) {
            BigDecimal availableBalance = entity.getAccount().getAvailableBalance()
                    .add(entity.getValue().abs());
            updateAccountBalance(entity.getAccount(), availableBalance);
        }

        repository.deleteById(id);
    }

    public void batchDelete(Long id) {
        TransactionEntity entity = findById(id);
        List<TransactionEntity> entities = repository.findBySignature(entity.getSignature())
                .stream().filter(t -> t.getInstallment() >= entity.getInstallment())
                .toList();

        Map<AccountEntity, BigDecimal> accumulatedBalance = entities.stream()
                .filter(TransactionEntity::getPaid)
                .collect(Collectors.groupingBy(
                        TransactionEntity::getAccount,
                        Collectors.reducing(BigDecimal.ZERO, TransactionEntity::getValue, BigDecimal::add)
                ));

        accumulatedBalance.forEach((account, balance) -> updateAccountBalance(account, account.getAvailableBalance().add(balance.abs())));

        repository.deleteAll(entities);
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

    protected void validatedSave(Transaction transaction) {
        if ("FIXED".equals(transaction.getPaymentType()) && (transaction.getFrequency() == null || transaction.getFrequency().isEmpty())) {
            throw new NotificationException("Erro ao salvar, para lançamento fixo é necessário informar a frenquência.");
        }
        if ("IN_INSTALLMENTS".equals(transaction.getPaymentType()) && (transaction.getFrequency() == null || transaction.getFrequency().isEmpty() || Integer.valueOf(0).equals(transaction.getInstallment()))) {
            throw new NotificationException("Erro ao salvar, para lançamento parcelado é necessário informar a frenquência e o número de parcelas.");
        }
    }

    protected void updateAvailableBalance(Transaction transaction, Long accountId) {
        if (isPaymentStatusChanged(transaction)) {
            AccountEntity account = accountService.findById(accountId);
            BigDecimal availableBalance = account.getAvailableBalance();
            if (transaction.getPaid()) {
                availableBalance = availableBalance.subtract(transaction.getValue());
            } else {
                availableBalance = availableBalance.add(transaction.getValue());
            }
            updateAccountBalance(account, availableBalance);
        }
    }

    protected void updateAccountBalance(AccountEntity account, BigDecimal balance) {
        account.setAvailableBalance(balance);
        accountService.save(account);
    }

    protected boolean isPaymentStatusChanged(final Transaction transaction) {
        if (transaction.getId() == null) {
            return transaction.getPaid();
        }
        return isPaymentStatusChanged(transaction.getId(), transaction.getPaid());
    }

    protected boolean isPaymentStatusChanged(final Long id, Boolean paid) {
        if (id == null) {
            return paid;
        }
        TransactionEntity entity = findById(id);
        return !entity.getPaid().equals(paid);
    }
}
