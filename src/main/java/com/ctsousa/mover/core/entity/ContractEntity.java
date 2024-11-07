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
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "initial_date", nullable = false)
    private LocalDate initialDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "billing_start_date", nullable = false)
    private LocalDate billingStartDate;

    @Column(name = "deposit_amount", nullable = false)
    private BigDecimal depositAmount;

    @Column(name = "recurrence_value", nullable = false)
    private BigDecimal recurrenceValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency", nullable = false)
    private PaymentFrequency paymentFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation", nullable = false)
    private Situation situation;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_day", nullable = false)
    private DayOfWeek paymentDay;

    @Column(name = "reason", columnDefinition = "LONGTEXT")
    private String reason;
}
