package com.ctsousa.mover.entity;

import com.ctsousa.mover.enumeration.DayOfWeek;
import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.Situation;
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
public class ContractEntity extends AbstractEntity {

    private CarEntity car;
    private ClientEntity client;
    private String number;
    private LocalDate initialDate;
    private LocalDate endDate;
    private LocalDate firstBillingDate;
    private BigDecimal rentDeposit;
    private BigDecimal value;
    private PaymentFrequency paymentFrequency;
    private Situation situation;
    private DayOfWeek day;
}
