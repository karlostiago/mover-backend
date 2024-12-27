package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.DateFormat;
import com.ctsousa.mover.core.annotation.NotEmpty;
import com.ctsousa.mover.core.deserializer.LocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionRequest {
    private Long id;

    @NotEmpty(message = "Descrição não pode ser vázio")
    private String description;

    @NotEmpty(message = "Categoria não pode ser vázio")
    private Long subcategoryId;

    private Integer installment;
    private String frequency;
    private String paymentType;

    @NotEmpty(message = "Tipo categoria não pode ser vázio")
    private String categoryType;

    @DateFormat(message = "Data vencimento inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dueDate;

    @DateFormat(message = "Data pagamento inválida")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate paymentDate;

    @NotEmpty(message = "Valor não pode ser vázio")
    private BigDecimal value;
    private BigDecimal installmentValue;
    private Long cardId;

    @NotEmpty(message = "Conta não pode ser vázio")
    private Long accountId;

    private Long destinationAccountId;
    private Long vehicleId;
    private Long contractId;
    private Long partnerId;
    private Boolean paid;
    private Boolean active;
}
