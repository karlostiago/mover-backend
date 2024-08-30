package com.ctsousa.mover.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {
    private Long id;
    private String name;
    private String accountNumber;
    private Integer codeIcon;
    private BigDecimal InitialBalance;
    private Boolean caution;
    private Boolean active;
}
