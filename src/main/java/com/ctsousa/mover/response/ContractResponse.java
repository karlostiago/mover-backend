package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.DayOfWeek;
import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.Situation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContractResponse {
    private Long id;
    private Long vehicleId;
    private Long clientId;
    private String vehicleName;
    private String clientName;
    private String number;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate initialDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate billingStartDate;
    private BigDecimal depositAmount;
    private BigDecimal recurrenceValue;
    private String paymentFrequency;
    private String situation;
    private String paymentDay;
    private String reason;
    private Boolean active;

    public void setPaymentFrequency(PaymentFrequency paymentFrequency) {
        this.paymentFrequency = paymentFrequency.getDescription();
    }

    public void setSituation(Situation situation) {
        this.situation = situation.getDescription();
    }

    public void setPaymentDay(DayOfWeek paymentDay) {
        this.paymentDay = paymentDay.getDescription();
    }
}
