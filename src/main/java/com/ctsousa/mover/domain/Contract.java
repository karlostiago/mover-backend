package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.ClientEntity;
import com.ctsousa.mover.core.entity.ContractEntity;
import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.enumeration.DayOfWeek;
import com.ctsousa.mover.enumeration.PaymentFrequency;
import com.ctsousa.mover.enumeration.Situation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Contract extends DomainModel<ContractEntity> {

    private Long vehicleId;
    private Long clientId;
    private String number;
    private LocalDate initialDate;
    private LocalDate endDate;
    private LocalDate billingStartDate;
    private BigDecimal depositAmount;
    private BigDecimal recurrenceValue;
    private String paymentFrequency;
    private String situation;
    private String paymentDay;
    private String reason;

    @Override
    public ContractEntity toEntity() {
        ContractEntity entity = new ContractEntity();
        entity.setId(this.getId());
        entity.setVehicle(new VehicleEntity(this.getVehicleId()));
        entity.setClient(new ClientEntity(this.getClientId()));
        entity.setNumber(this.getNumber());
        entity.setInitialDate(this.getInitialDate());
        entity.setEndDate(this.getEndDate());
        entity.setBillingStartDate(this.getBillingStartDate());
        entity.setDepositAmount(this.getDepositAmount());
        entity.setRecurrenceValue(this.getRecurrenceValue());
        entity.setReason(this.getReason());

        PaymentFrequency paymentFrequency = PaymentFrequency.toDescription(this.getPaymentFrequency());
        entity.setPaymentFrequency(paymentFrequency);

        Situation situation = Situation.toDescription(this.getSituation());
        entity.setSituation(situation);

        DayOfWeek dayOfWeek = DayOfWeek.toDescription(this.getPaymentDay());
        entity.setPaymentDay(dayOfWeek);

        entity.setActive(this.getActive());
        return entity;
    }
}
