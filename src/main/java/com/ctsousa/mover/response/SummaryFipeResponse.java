package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
public class SummaryFipeResponse {
    private final BigDecimal valueAcquisition;
    private final String referenceAcquisition;
    private final BigDecimal valueMonthCurrent;
    private final String referenceMonthCurrent;

    @Setter
    private BigDecimal depreciatedValue;

    @Setter
    private Double percentageDepreciated;

    public SummaryFipeResponse(BigDecimal valueAcquisition, String referenceAcquisition, BigDecimal valueMonthCurrent, String referenceMonthCurrent) {
        this.valueAcquisition = valueAcquisition;
        this.referenceAcquisition = referenceAcquisition;
        this.valueMonthCurrent = valueMonthCurrent;
        this.referenceMonthCurrent = referenceMonthCurrent;
    }
}
