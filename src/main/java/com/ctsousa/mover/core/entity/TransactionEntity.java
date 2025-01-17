package com.ctsousa.mover.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_transaction")
public class TransactionEntity extends AbstractEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subcategory_id", nullable = false)
    private SubCategoryEntity subcategory;

    @Column(name = "installment")
    private int installment;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "frequency")
    private String frequency;

    @Column(name = "category_type", nullable = false)
    private String categoryType;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "`value`", nullable = false)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private CardEntity card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private ContractEntity contract;

    @JoinColumn(name = "signature", nullable = false)
    private String signature;

    @JoinColumn(name = "transaction_type", nullable = false)
    private String transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private PartnerEntity partner;

    @Column(name = "paid", nullable = false)
    private Boolean paid;

    @Column(name = "predicted", nullable = false)
    private Boolean predicted;

    @Column(name = "refund", nullable = false)
    private Boolean refund;

    @Column(name = "hour", nullable = false)
    private LocalTime hour;

    @Transient
    private AccountEntity destinationAccount;
}
