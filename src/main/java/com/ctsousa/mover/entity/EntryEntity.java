package com.ctsousa.mover.entity;

import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.TypeEntry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntryEntity extends AbstractEntity {

    private AccountEntity account;
    private CardEntity card;
    private PartnerEntity partner;
    private TypeEntry type;
    private CategoryEntity category;
    private Subcategory subcategory;
    private CarEntity car;
    private LocalDate date;
    private BigDecimal value;
    private String decription;
    private Boolean effective;
    private String observation;
    private String tag;
    private String attachment;
    private BigDecimal fixedExpense;
    private PaymentFrequency frequency;
    private Boolean installment;
    private int installmentQuantity;
}
