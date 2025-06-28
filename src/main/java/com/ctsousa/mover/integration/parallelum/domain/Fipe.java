package com.ctsousa.mover.integration.parallelum.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fipe extends Domain {
    private String vehicleType;
    private String price;
    private String brand;
    private String model;
    private Integer modelYear;
    private String fuel;
    private String codeFipe;
    private String referenceMonth;
}
