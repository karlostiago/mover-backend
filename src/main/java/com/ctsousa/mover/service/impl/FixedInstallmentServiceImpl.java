package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.service.FixedInstallmentService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl.calculateDueDate;
import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;
import static com.ctsousa.mover.enumeration.TypeCategory.toDescription;

@Component
public class FixedInstallmentServiceImpl implements FixedInstallmentService {

    private final int quantityInstallment = 1000;

    @Override
    public List<TransactionEntity> generated(Transaction transaction) {
        String signature = null;

        List<TransactionEntity> entities = generatedToTransfer(transaction, toDescription(transaction.getCategoryType()));

        if (entities == null) {
            entities = new ArrayList<>(quantityInstallment);
            for (int installment = 0; installment < quantityInstallment; installment++) {
                TransactionEntity entity = transaction.toEntity();
                signature = signature == null ? entity.getSignature() : signature;
                entity.setInstallment(installment + 1);
                entity.setDueDate(calculateDueDate(entity.getDueDate(), transaction.getFrequency(), installment));
                entity.setSignature(signature);
                entity.setTransactionType(transaction.getTransactionType());
                entity.setPredicted(Boolean.TRUE);
                entities.add(entity);
            }
        }

        return entities;
    }

    private List<TransactionEntity> generatedToTransfer(Transaction transaction, TypeCategory category) {
        if (!TypeCategory.TRANSFER.equals(category)) return null;

        AccountEntity creditAccount = new AccountEntity(transaction.getDestinationAccount().getId());
        AccountEntity debitAccount = new AccountEntity((transaction.getAccount().getId()));
        String signature = null;

        List<TransactionEntity> entities = new ArrayList<>();
        for (int installment = 0; installment < quantityInstallment; installment++) {
            TransactionEntity creditEntity = transaction.toEntity();
            signature = signature == null ? creditEntity.getSignature() : signature;

            creditEntity.setTransactionType(TransactionType.CREDIT.name());
            creditEntity.setAccount(creditAccount);
            creditEntity.setInstallment(installment + 1);
            creditEntity.setDueDate(calculateDueDate(creditEntity.getDueDate(), creditEntity.getFrequency(), installment));
            creditEntity.setSignature(signature);
            creditEntity.setPredicted(Boolean.TRUE);
            entities.add(creditEntity);

            TransactionEntity debitEntity = transaction.toEntity();
            debitEntity.setTransactionType(TransactionType.DEBIT.name());
            debitEntity.setAccount(debitAccount);
            debitEntity.setValue(invertSignal(creditEntity.getValue()));
            debitEntity.setDueDate(creditEntity.getDueDate());
            debitEntity.setSignature(creditEntity.getSignature());
            debitEntity.setInstallment(creditEntity.getInstallment());
            debitEntity.setDescription(creditEntity.getDescription());
            debitEntity.setPredicted(creditEntity.getPredicted());
            entities.add(debitEntity);
        }

        return entities;
    }

    @Override
    public boolean isFixed(Transaction transaction) {
        return transaction.getPaymentType() != null && "FIXED".equals(transaction.getPaymentType());
    }
}
