package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.ctsousa.mover.service.InstallmentService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.service.impl.BaseTransactionServiceImpl.calculateDueDate;
import static com.ctsousa.mover.core.util.NumberUtil.invertSignal;
import static com.ctsousa.mover.enumeration.TypeCategory.toDescription;

@Component
public class InstallmentServiceImpl implements InstallmentService {

    private static final long FIRST_INSTALLMENT = 0L;

    @Override
    public List<TransactionEntity> generated(Transaction transaction) {
        int quantityInstallment = transaction.getInstallment();
        BigDecimal totalValue = transaction.getValue();
        BigDecimal installmentValue = totalValue.divide(BigDecimal.valueOf(quantityInstallment), 2, RoundingMode.HALF_UP);
        BigDecimal differentValue = calculateDifferentInstallmentValue(quantityInstallment, installmentValue, totalValue);
        String signature = null;

        List<TransactionEntity> entities = generatedToTransfer(transaction, toDescription(transaction.getCategoryType()));

        if (entities == null) {
            entities = new ArrayList<>();
            for (int installment = 0; installment < quantityInstallment; installment++) {
                TransactionEntity entity = transaction.toEntity();
                signature = signature == null ? entity.getSignature() : signature;

                entity.setValue(calculateInstallmentValue(quantityInstallment, differentValue, installmentValue, installment));
                entity.setDueDate(calculateDueDate(entity.getDueDate(), entity.getFrequency(), installment));
                entity.setTransactionType(transaction.getTransactionType());
                entity.setInstallment(installment + 1);
                entity.setSignature(signature);
                entity.setDescription(entity.getDescription() + " (" + entity.getInstallment() + ")");

                removePaymentDateAndPaidAfterFirstInstallment(installment, entity);

                entities.add(entity);
            }
        }

        return entities;
    }

    @Override
    public boolean hasInstallment(Transaction transaction) {
        return transaction.getInstallment() != null && transaction.getInstallment() > 1;
    }

    private List<TransactionEntity> generatedToTransfer(Transaction transaction, TypeCategory category) {
        if (!TypeCategory.TRANSFER.equals(category)) return null;

        int quantityInstallment = transaction.getInstallment();
        BigDecimal totalValue = transaction.getValue();
        BigDecimal installmentValue = totalValue.divide(BigDecimal.valueOf(quantityInstallment), 2, RoundingMode.HALF_UP);
        BigDecimal differentValue = calculateDifferentInstallmentValue(quantityInstallment, installmentValue, totalValue);

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
            creditEntity.setValue(calculateInstallmentValue(quantityInstallment, differentValue, installmentValue, installment));
            creditEntity.setDueDate(calculateDueDate(creditEntity.getDueDate(), creditEntity.getFrequency(), installment));
            creditEntity.setDescription(creditEntity.getDescription() + " (" + creditEntity.getInstallment() + ")");
            creditEntity.setSignature(signature);

            removePaymentDateAndPaidAfterFirstInstallment(installment, creditEntity);
            entities.add(creditEntity);

            TransactionEntity debitEntity = transaction.toEntity();
            debitEntity.setTransactionType(TransactionType.DEBIT.name());
            debitEntity.setAccount(debitAccount);
            debitEntity.setValue(invertSignal(creditEntity.getValue()));
            debitEntity.setDueDate(creditEntity.getDueDate());
            debitEntity.setSignature(creditEntity.getSignature());
            debitEntity.setInstallment(creditEntity.getInstallment());
            debitEntity.setDescription(creditEntity.getDescription());
            entities.add(debitEntity);
        }

        return entities;
    }

    private void removePaymentDateAndPaidAfterFirstInstallment(int installment, TransactionEntity entity) {
        if (installment > 0) {
            entity.setPaymentDate(null);
            entity.setPaid(Boolean.FALSE);
        }
    }

    private BigDecimal calculateDifferentInstallmentValue(Integer quantityInstallment, BigDecimal installmentValue, BigDecimal totalValue) {
        BigDecimal calculatedValue = installmentValue.multiply(BigDecimal.valueOf(quantityInstallment));
        return totalValue.subtract(calculatedValue);
    }

    private BigDecimal calculateInstallmentValue(Integer quantityInstallment, BigDecimal differentValue, BigDecimal installmentValue, long installment) {
        if (differentValue.compareTo(BigDecimal.ZERO) < 0) {
            return installment == (quantityInstallment - 1) ? installmentValue.add(differentValue) : installmentValue;
        } else if (differentValue.compareTo(BigDecimal.ZERO) > 0) {
            return installment == FIRST_INSTALLMENT ? installmentValue.add(differentValue) : installmentValue;
        } else {
            return installmentValue;
        }
    }
}
