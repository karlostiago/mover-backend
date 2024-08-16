package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.enumeration.Situation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class Vehicle extends DomainModel<VehicleEntity> {
    private Long id;
    private Model model;
    private Brand brand;
    private String licensePlate;
    private Integer yearManufacture;
    private Integer modelYear;
    private String renavam;
    private BigDecimal fipeValueAtAcquisition;
    private BigDecimal acquisitionValue;
    private LocalDate acquisitionDate;
    private LocalDate availabilityDate;
    private BigDecimal mileageAtAcquisition;
    private Boolean auction;
    private BigDecimal fipeDepreciation;
    private String color;
    private Integer codeSituation;

    @Override
    public VehicleEntity toEntity() {
        VehicleEntity entity = new VehicleEntity();
        entity.setId(this.getId());
        entity.setLicensePlate(this.getLicensePlate());
        entity.setYearManufacture(this.getYearManufacture());
        entity.setModelYear(this.getModelYear());
        entity.setRenavam(this.getRenavam());
        entity.setFipeValueAtAcquisition(this.getFipeValueAtAcquisition());
        entity.setAcquisitionValue(this.getAcquisitionValue());
        entity.setAcquisitionDate(this.getAcquisitionDate());
        entity.setAvailabilityDate(this.getAvailabilityDate());
        entity.setMileageAtAcquisition(this.getMileageAtAcquisition());
        entity.setAuction(this.getAuction());
        entity.setFipeDepreciation(this.getFipeDepreciation());
        entity.setColor(this.getColor().toUpperCase());

        Situation situation = Situation.toCode(codeSituation);
        entity.setSituation(situation.getDescription());
        return entity;
    }
}
