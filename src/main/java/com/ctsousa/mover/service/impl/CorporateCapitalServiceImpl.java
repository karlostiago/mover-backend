package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.repository.TransactionRepository;
import com.ctsousa.mover.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;

@Component
public class CorporateCapitalServiceImpl extends BaseTransactionServiceImpl implements CorporateCapitalService {

    @Autowired
    private final AccountService accountService;

    private final InstallmentService installmentService;
    private final FixedInstallmentService fixedInstallmentService;

    public CorporateCapitalServiceImpl(TransactionRepository repository, AccountService accountService, InstallmentService installmentService, FixedInstallmentService fixedInstallmentService) {
        super(repository, accountService, null, null);
        this.accountService = accountService;
        this.installmentService = installmentService;
        this.fixedInstallmentService = fixedInstallmentService;
    }

    @Override
    public TransactionEntity save(Transaction transaction) {
        validatedSave(transaction);
        TransactionEntity entity = transaction.toEntity();
        TransactionEntity entitySaved = repository.save(entity);

        boolean hasInstallment = installmentService.hasInstallment(transaction);
        boolean isFixed = fixedInstallmentService.isFixed(transaction);

        return entitySaved;
    }

    @Override
    public TransactionEntity update(Transaction transaction) {
        String signature = repository.findBySignature(transaction.getId());
        TransactionEntity entity = transaction.toEntity();
        entity.setSignature(signature);
        return repository.save(entity);
    }

//    @Override
//    public TransactionEntity update(Transaction transaction) {
//        TransactionEntity originalTransaction = findById(transaction.getId());
//        String signature = originalTransaction.getSignature();
//
//        boolean wasPaid = originalTransaction.getPaid();
//        boolean isNowPaid = transaction.getPaid();
//
//        TransactionEntity entity = transaction.toEntity();
//        entity.setTransactionType(TransactionType.DEBIT.name());
//        entity.setAccount(accountService.findById(transaction.getAccount().getId()));
//        entity.setSignature(signature);
//        entity.setValue(invertSignal(transaction.getValue()));
//
//        if ("IN_INSTALLMENTS".equals(originalTransaction.getPaymentType())) {
//            entity.setInstallment(originalTransaction.getInstallment());
//            entity.setFrequency(originalTransaction.getFrequency());
//            entity.setPaymentType(originalTransaction.getPaymentType());
//        }
//
//        if ("FIXED".equals(originalTransaction.getPaymentType())) {
//            entity.setInstallment(originalTransaction.getInstallment());
//            entity.setFrequency(originalTransaction.getFrequency());
//            entity.setPaymentType(originalTransaction.getPaymentType());
//            entity.setPredicted(Boolean.TRUE);
//        }
//
//        if (wasPaid && !isNowPaid) {
//            updateBalance(entity.getAccount(), transaction.getValue());
//        } else if (!wasPaid && isNowPaid) {
//            updateBalance(entity.getAccount(), invertSignal(transaction.getValue()));
//        }
//
//        return repository.save(entity);
//    }

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
}
