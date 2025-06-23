package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardDashboardResponse {
    private String description;
    private Integer quantity = 0;
    private BigDecimal value = BigDecimal.ZERO;
    private String iconPath;
    private boolean loading;
}
