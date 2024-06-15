package com.ctsousa.mover.entity;

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
    @JoinColumn(name = "car_id", nullable = false)
    private CarEntity car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "mileage", nullable = false)
    private Long mileage;

    @Column(name = "workshop", nullable = false)
    private String workshop;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "observation", nullable = false)
    private String observation;

    @Column(name = "attachment", nullable = false)
    private String attachment;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
}
