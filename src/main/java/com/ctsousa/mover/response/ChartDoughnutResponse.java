package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ChartDoughnutResponse {
    private List<String> labels;
    private List<BigDecimal> values;
}
