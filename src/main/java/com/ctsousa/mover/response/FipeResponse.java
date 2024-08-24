package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FipeResponse {
    private String brand;
    private String model;
    private Integer modelYear;
    private String referenceMonth;
    private Integer referenceYear;
    private String fuel;
    private String code;
    private BigDecimal price;
}
