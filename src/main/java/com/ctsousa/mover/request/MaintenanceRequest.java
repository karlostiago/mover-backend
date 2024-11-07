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
public class MaintenanceRequest {
    private Long id;

    @NotEmpty(message = "Veículo não pode ser vázio")
    private Long vehicleId;

    @NotEmpty(message = "Conta não pode ser vázio")
    private Long accountId;

    private Long cardId;

    @DateFormat(message = "Data inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @NotEmpty(message = "Data não pode ser vázio")
    private LocalDate date;

    @NotEmpty(message = "Km não pode ser vázio")
    private Long mileage;

    @NotEmpty(message = "Estabelecimento não pode ser vázio")
    private String establishment;

    @NotEmpty(message = "Tipo não pode ser vázio")
    private String type;

    @NotEmpty(message = "Detalhe não pode ser vázio")
    private String detail;

    @NotEmpty(message = "Valor não pode ser vázio")
    private BigDecimal value;

    private Boolean active;
}
