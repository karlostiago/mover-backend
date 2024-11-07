package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.DateFormat;
import com.ctsousa.mover.core.annotation.NotEmpty;
import com.ctsousa.mover.core.deserializer.LocalDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContractRequest {

    private Long id;

    @NotEmpty(message = "Veículo não pode ser vázio")
    private Long vehicleId;

    @NotEmpty(message = "Clinte não pode ser vázio")
    private Long clientId;

    @NotEmpty(message = "Número não pode ser vázio")
    private String number;

    @DateFormat(message = "Data inicial inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty(message = "Data inicial não pode ser vázio")
    private LocalDate initialDate;

    @DateFormat(message = "Data término inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate endDate;

    @DateFormat(message = "Data inicio cobrança inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty(message = "Data inicio cobrança não pode ser vázio")
    private LocalDate billingStartDate;

    @NotEmpty(message = "Valor caução não pode ser vázio")
    private BigDecimal depositAmount;

    @NotEmpty(message = "Valor recorrência não pode ser vázio")
    private BigDecimal recurrenceValue;

    @NotEmpty(message = "Frequência pagamento não pode ser vázio")
    private String paymentFrequency;

    @NotEmpty(message = "Situação não pode ser vázio")
    private String situation;

    @NotEmpty(message = "Dia pagamento não pode ser vázio")
    private String paymentDay;

    private String reason;

    private Boolean active;

    private Boolean friendlyTermination;
}
