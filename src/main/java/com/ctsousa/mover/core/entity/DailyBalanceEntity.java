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
@Table(name = "tb_daily_balance",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"account_id", "period"})
    }
)
public class DailyBalanceEntity extends AbstractEntity {

    @Column(name = "period", nullable = false)
    private LocalDate period;

    @Column(name = "balance")
    private BigDecimal balance;

    @JoinColumn(name = "account_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private AccountEntity account;
}
