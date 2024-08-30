package com.ctsousa.mover.core.entity;

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

    @Column(name = "icon", columnDefinition = "LONGTEXT")
    private String icon;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "initial_balance", nullable = false)
    private BigDecimal InitialBalance;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "caution", nullable = false)
    private Boolean caution;
}
