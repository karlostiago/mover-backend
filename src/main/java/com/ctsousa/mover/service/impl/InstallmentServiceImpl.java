package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.service.InstallmentService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InstallmentServiceImpl implements InstallmentService {

    @Override
    public List<TransactionEntity> generated(Transaction transaction) {
        Integer quantityInstallment = transaction.getInstallment();
        BigDecimal totalValue = transaction.getValue();
        BigDecimal installmentValue = totalValue.divide(BigDecimal.valueOf(quantityInstallment), 2, RoundingMode.HALF_UP);
        BigDecimal firstInstallmentValue = calculateFirtsInstallmentValue(quantityInstallment, installmentValue, totalValue);
        String signature = null;

        List<TransactionEntity> entities = new ArrayList<>();

        for (int index = 0; index < quantityInstallment; index++) {
            TransactionEntity entity = transaction.toEntity();
            signature = signature == null ? entity.getSignature() : signature;
            entity.setValue(index == 0 ? firstInstallmentValue : installmentValue);
            entity.setSignature(signature);
            entity.setDueDate(calculateDueDate(entity.getDueDate(), entity.getFrequency(), index));
            entity.setInstallment(index + 1);
            entities.add(entity);
        }

        return entities;
    }

    @Override
    public boolean hasInstallment(Transaction transaction) {
        return transaction.getInstallment() != null && transaction.getInstallment() > 1;
    }

    private LocalDate calculateDueDate(LocalDate dueDate, String fraquency, long position) {
        if (position == 0) return dueDate;
        if ("MONTHLY".equals(fraquency)) {
            long quantityDays = 30 * position;
            return dueDate.plusDays(quantityDays);
        }
        return null;
    }

    private BigDecimal calculateFirtsInstallmentValue(Integer quantityInstallment, BigDecimal installmentValue, BigDecimal totalValue) {
        BigDecimal calculatedValue = installmentValue.multiply(BigDecimal.valueOf(quantityInstallment));
        BigDecimal differenceValue = totalValue.subtract(calculatedValue);

        if (differenceValue.compareTo(BigDecimal.ZERO) == 0) {
            return installmentValue;
        }

        return installmentValue.add(differenceValue);
    }
}
