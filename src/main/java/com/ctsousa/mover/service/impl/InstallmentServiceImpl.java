package com.ctsousa.mover.service.impl;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.domain.Transaction;
import com.ctsousa.mover.enumeration.PaymentFrequency;
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
        BigDecimal differentValue = calculateDifferentValue(quantityInstallment, installmentValue, totalValue);
        String signature = null;

        List<TransactionEntity> entities = new ArrayList<>();

        for (int installment = 0; installment < quantityInstallment; installment++) {
            TransactionEntity entity = transaction.toEntity();
            signature = signature == null ? entity.getSignature() : signature;

            entity.setValue(calculateInstallmentValue(quantityInstallment, differentValue, installmentValue, installment));
            entity.setSignature(signature);
            entity.setDueDate(calculateDueDate(entity.getDueDate(), entity.getFrequency(), installment));
            entity.setInstallment(installment + 1);
            entity.setTransactionType(transaction.getTransactionType());
            entities.add(entity);
        }

        return entities;
    }

    @Override
    public boolean hasInstallment(Transaction transaction) {
        return transaction.getInstallment() != null && transaction.getInstallment() > 1;
    }

    private LocalDate calculateDueDate(LocalDate dueDate, String frequency, long installment) {
        if (installment == 0) return dueDate;

        try {
            PaymentFrequency paymentFrequency = PaymentFrequency.toDescription(frequency);
            return dueDate.plusDays(paymentFrequency.days() * installment);
        } catch (NotificationException e) {
            throw new NotificationException("Não há suporte para calcular a data de vencimento para a frequência informada.");
        }
//        return switch (frequency) {
//            case "DAILY" : yield dueDate.plusDays(installment);
//            case "WEEKLY" : yield dueDate.plusDays(7 * installment);
//            case "BIWEEKLY" : yield dueDate.plusDays(15 * installment);
//            case "MONTHLY" : yield dueDate.plusDays(30 * installment);
//            case "BIMONTHLY" : yield dueDate.plusDays(60 * installment);
//            case "QUARTERLY" : yield dueDate.plusDays(90 * installment);
//            case "SEMIANNUAL" : yield dueDate.plusDays(180 * installment);
//            case "ANNUAL" : yield dueDate.plusDays(360 * installment);
//            default:
//                throw new NotificationException("Não há suporte para calcular a data de vencimento para a frequência ::: " + frequency);
//        };
    }

    private BigDecimal calculateDifferentValue(Integer quantityInstallment, BigDecimal installmentValue, BigDecimal totalValue) {
        BigDecimal calculatedValue = installmentValue.multiply(BigDecimal.valueOf(quantityInstallment));
        return totalValue.subtract(calculatedValue);
    }

    private BigDecimal calculateInstallmentValue(Integer quantityInstallment, BigDecimal differentValue, BigDecimal installmentValue, long position) {
        if (differentValue.compareTo(BigDecimal.ZERO) < 0) {
            return position == (quantityInstallment - 1) ? installmentValue.add(differentValue) : installmentValue;
        } else if (differentValue.compareTo(BigDecimal.ZERO) > 0) {
            return position == 0 ? installmentValue.add(differentValue) : installmentValue;
        } else {
            return installmentValue;
        }
    }
}
