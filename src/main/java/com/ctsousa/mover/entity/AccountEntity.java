package com.ctsousa.mover.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity extends AbstractEntity {

    private String name;
    private String icon;
    private BigDecimal InitialBalance;
}
