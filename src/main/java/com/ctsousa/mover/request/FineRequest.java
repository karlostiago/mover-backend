package com.ctsousa.mover.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class FineRequest {
    private Long id;
    private String description;
    private String infractionNotice;
    private String numberRenainf;
    private BigInteger infractionCode;
    private LocalDateTime dateTimeOfCommitment;
    private LocalDate dueDate;
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
