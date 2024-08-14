package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.DayOfWeek;
import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.Situation;
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
@Table(name = "tb_contract")
public class ContractEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private VehicleEntity car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "initial_date", nullable = false)
    private LocalDate initialDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "first_billing_date", nullable = false)
    private LocalDate firstBillingDate;

    @Column(name = "rent_deposit", nullable = false)
    private BigDecimal rentDeposit;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency", nullable = false)
    private PaymentFrequency paymentFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation", nullable = false)
    private Situation situation;

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_day", nullable = false)
    private DayOfWeek billingDay;
}
