package com.ctsousa.mover.response;

import com.ctsousa.mover.core.util.NumberUtil;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FipeValueResponse {
    private final BigDecimal value;

    public FipeValueResponse(String value) {
        this.value = NumberUtil.toBigDecimal(value);
    }
}
