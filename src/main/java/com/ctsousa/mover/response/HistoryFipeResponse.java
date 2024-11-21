package com.ctsousa.mover.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class HistoryFipeResponse {
    private String monthReference;
    private String fipeCode;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    private String vehicleFullname;
}
