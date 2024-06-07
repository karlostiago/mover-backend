package com.ctsousa.mover.entity;

import com.ctsousa.mover.enumeration.Situation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarEntity extends AbstractEntity {

    private BrandEntity brand;
    private ModelEntity model;
    private String licensePlate;
    private String nationalVehicleRegistry;
    private LocalDate fipeAcquisitionDate;
    private BigDecimal price;
    private LocalDate acquisitionDate;
    private LocalDate readyDate;
    private BigDecimal kmAquisition;
    private Boolean auction = Boolean.FALSE;
    private BigDecimal fipeDevaluation;
    private Situation situation;
}
