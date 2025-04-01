package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.entity.PaymentDetailEntity;
import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class PaymentDetail extends DomainModel<PaymentDetailEntity> {
    private LocalDate date;
    private BigDecimal amountPaid;
    private TransactionEntity invoice;
    private AccountEntity account;
    private CardEntity card;

    @Override
    public PaymentDetailEntity toEntity() {

        if (account == null && card == null) {
            throw new NotificationException("O pagamento deve estar associado a uma conta ou cartão.");
        }

        PaymentDetailEntity entity = new PaymentDetailEntity();
        entity.setId(getId());
        entity.setDate(getDate());
        entity.setAmountPaid(getAmountPaid());
        entity.setActive(getActive());
        entity.setAccount(getAccount());
        entity.setCard(getCard());
        return entity;
    }
}
