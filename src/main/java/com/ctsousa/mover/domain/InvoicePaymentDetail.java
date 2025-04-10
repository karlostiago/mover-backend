package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.InvoicePaymentDetailEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class InvoicePaymentDetail extends DomainModel<InvoicePaymentDetailEntity> {
    private LocalDate date;
    private BigDecimal value;
    private TransactionEntity invoice;
    private AccountEntity account;
    private TransactionEntity payment;

    @Override
    public InvoicePaymentDetailEntity toEntity() {

        if (account == null) {
            throw new NotificationException("O pagamento deve estar associado a uma conta.");
        }

        InvoicePaymentDetailEntity entity = new InvoicePaymentDetailEntity();
        entity.setId(getId());
        entity.setDate(getDate());
        entity.setValue(getValue());
        entity.setActive(getActive());
        entity.setAccount(getAccount());
        entity.setPayment(getPayment());
        return entity;
    }
}
