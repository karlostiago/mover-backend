package com.ctsousa.mover.response;

import com.ctsousa.mover.core.annotation.DateFormat;
import com.ctsousa.mover.core.deserializer.LocalDateDeserializer;
import com.ctsousa.mover.enumeration.CardType;
import com.ctsousa.mover.enumeration.Icon;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class FineResponse {
    private Long id;
    private String description;
    private String infractionNotice;
    private BigInteger numberRenainf;
    private BigInteger infractionCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateTimeOfCommitment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate expirationInfraction;
    private Long clientId;
    private Long vehicleId;
    private BigDecimal value;
    private BigDecimal originalValue;
    private BigDecimal discount;
    private Boolean realOffender;
    private Long accountId;
    private Long cardId;
}
