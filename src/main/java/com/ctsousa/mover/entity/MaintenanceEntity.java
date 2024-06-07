package com.ctsousa.mover.entity;

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
public class MaintenanceEntity extends AbstractEntity {

    private CarEntity car;
    private AccountEntity account;
    private CardEntity card;
    private LocalDate date;
    private Long km;
    private String workshop;
    private String tipo;
    private String description;
    private String observation;
    private String attachment;
    private BigDecimal value;
}
