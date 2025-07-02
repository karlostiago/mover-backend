package com.ctsousa.mover.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "tb_account_balance_photo")
public class AccountBalancePhotoEntity extends AbstractEntity {

    @Column(name = "period", nullable = false)
    private LocalDate period;

    @Column(name = "account_balance", nullable = false)
    private BigDecimal accountBalance;
}
