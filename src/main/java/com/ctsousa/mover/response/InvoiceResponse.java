package com.ctsousa.mover.response;

import com.ctsousa.mover.core.entity.TransactionEntity;
import com.ctsousa.mover.enumeration.Icon;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ctsousa.mover.core.mapper.Transform.toMapper;

@Setter
@Getter
public class InvoiceResponse {

    private LocalDate dueDate;
    private LocalDate paymentDate;
    private Boolean paid;
    private BigDecimal total;
    private Long cardId;
    private Icon icon;
    private List<TransactionResponse> transactions;

    public InvoiceResponse(TransactionEntity.Invoice invoice) {
        cardId = invoice.getCard().getId();
        dueDate = invoice.getDueDate();
        icon = Icon.toName(invoice.getCard().getIcon());
        paymentDate = invoice.getPaymentDate();
        total = invoice.getTotal();

        transactions = new ArrayList<>();
        updateTransaction(invoice.getTransactions());
    }

    private void updateTransaction(List<TransactionEntity> entities) {
        for (TransactionEntity entity : entities) {
            String subcategory = entity.getSubcategory().getDescription();
            String category = entity.getSubcategory().getCategory().getDescription();
            TransactionResponse response = toMapper(entity, TransactionResponse.class);
            response.setSubcategory(subcategory);
            response.setCategory(category);
            if (response.getPaymentDate() != null) {
                response.setDate(response.getPaymentDate());
            } else {
                response.setDate(response.getDueDate());
            }
            response.setIcon(icon.getUrlImage());
            this.transactions.add(response);
        }
    }
}
