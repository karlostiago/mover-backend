package com.ctsousa.mover.response;

import com.ctsousa.mover.core.util.NumberUtil;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class FipeValueResponse {
    private final BigDecimal value;
    private final String code;

    public FipeValueResponse(String value, String code) {
        this.value = NumberUtil.toBigDecimal(value);
        this.code = code;
    }

    public FipeValueResponse(BigDecimal value, String code) {
        this.value = value;
        this.code = code;
    }
}
