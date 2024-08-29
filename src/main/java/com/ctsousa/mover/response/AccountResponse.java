package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountResponse {
    private Long id;
    private String name;
    private String icon;
    private BigDecimal InitialBalance;
    private Boolean caution;
    private Boolean active;
}
