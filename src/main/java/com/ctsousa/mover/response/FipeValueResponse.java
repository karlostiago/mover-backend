package com.ctsousa.mover.response;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FipeValueResponse {
    private BigDecimal value;

    public FipeValueResponse(String value) {

    }
}
