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
    private int codeTypeCategory;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate registerDate;

    private Boolean lastInstallment;

    private Long remainingPages;
    private String dayOfWeek;
    private Boolean scheduled = Boolean.FALSE;

    private String vehicle;
    private String account;
    private String card;
    private String contract;

    private Boolean invoice;
    private Long invoiceId;
    private BigDecimal residualValue;
    private BigDecimal amountPaid;

    public void setCategoryType(String categoryType) {
        TypeCategory type = TypeCategory.toDescription(categoryType);
        this.categoryType = type.getDescription();
        this.codeTypeCategory = type.getCode();

    }

    public void setTransactionType(String transactionType) {
        this.transactionType = TransactionType.toDescription(transactionType)
                .getDescription();
    }
}
