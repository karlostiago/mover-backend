package com.ctsousa.mover.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_account")
public class AccountEntity extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "icon")
    private String icon;

    @Column(name = "initial_balance", nullable = false)
    private BigDecimal InitialBalance;
}
