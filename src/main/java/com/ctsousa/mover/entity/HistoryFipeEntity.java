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
@Table(name = "history_fipe")
public class HistoryFipeEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "car_id", nullable = false)
    private CarEntity car;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "value", nullable = false)
    private BigDecimal value;
}
