package com.ctsousa.mover.core.entity;

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
@Table(name = "tb_maintenance")
public class MaintenanceEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private VehicleEntity vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private CardEntity card;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "mileage", nullable = false)
    private Long mileage;

    @Column(name = "establishment", nullable = false)
    private String establishment;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "detail", nullable = false, columnDefinition = "LONGTEXT")
    private String detail;

    @Column(name = "`value`", nullable = false)
    private BigDecimal value;
}
