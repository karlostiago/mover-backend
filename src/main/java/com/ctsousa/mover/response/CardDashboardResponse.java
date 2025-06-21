package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardDashboardResponse {
    private Integer quantity = 0;
    private BigDecimal value = BigDecimal.ZERO;
}
