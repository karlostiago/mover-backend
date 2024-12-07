package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BalanceResponse {
    private BigDecimal currentAccount;
    private BigDecimal escrowAccount;
    private BigDecimal creditCard;
}
