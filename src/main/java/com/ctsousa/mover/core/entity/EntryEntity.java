package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.TypeEntry;
import jakarta.persistence.*;
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
@Entity
@Table(name = "tb_entry")
public class EntryEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", nullable = false)
    private PartnerEntity partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeEntry type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubcategoryEntity subcategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private VehicleEntity car;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "description", nullable = false)
    private String decription;

    @Column(name = "effective", nullable = false)
    private Boolean effective;

    @Column(name = "observation")
    private String observation;

    @Column(name = "tag")
    private String tag;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "fixed_expense")
    private BigDecimal fixedExpense;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    private PaymentFrequency frequency;

    @Column(name = "installment", nullable = false)
    private Boolean installment;

    @Column(name = "installment_quantity", nullable = false)
    private int installmentQuantity;
}
