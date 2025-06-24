package com.ctsousa.mover.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CardDashboardResponse {
    private String description;
    private Integer quantity;
    private BigDecimal value;
    private String iconPath;
    private boolean loading;
}
