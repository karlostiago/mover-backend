package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.TransactionType;
import com.ctsousa.mover.enumeration.TypeCategory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionResponse {
    private Long id;
    private String icon;
    private String description;
    private Long subcategoryId;
    private Integer installment;
    private String frequency;
    private String paymentType;
    private String categoryType;
    private BigDecimal value;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate paymentDate;

    private Boolean paid;
    private Boolean active;

    private String category;
    private String subcategory;

    private String transactionType;

    private Long accountId;
    private Long destinationAccountId;
    private Long partnerId;
    private Long vehicleId;
    private Long contractId;
    private Long cardId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date;

    private Long remainingPages;

    public void setCategoryType(String categoryType) {
        this.categoryType = TypeCategory.toDescription(categoryType)
                .getDescription();
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = TransactionType.toDescription(transactionType)
                .getDescription();
    }
}
