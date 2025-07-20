package com.ctsousa.mover.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_fine")
public class FineEntity extends AbstractEntity {

    @Column(name = "infraction_notice", nullable = false)
    private String infractionNotice;

    @Column(name = "number_remainf", nullable = false)
    private BigInteger numberRenainf;

    @Column(name = "infraction_code", nullable = false)
    private BigInteger infractionCode;

    @Column(name = "date_time_of_commitment", nullable = false)
    private LocalDateTime dateTimeOfCommitment;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "expiration_infraction", nullable = false)
    private LocalDate expirationInfraction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @Column(name = "`value`", nullable = false)
    private BigDecimal value;

    @Column(name = "original_value", nullable = false)
    private BigDecimal originalValue;

    @Column(name = "description", nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "real_offender")
    private Boolean realOffender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private CardEntity card;
}
