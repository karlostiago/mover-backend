package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.VehicleEntity;
import com.ctsousa.mover.enumeration.FuelType;
import com.ctsousa.mover.enumeration.Situation;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class Vehicle extends DomainModel<VehicleEntity> {
    private Long id;
    private Model model;
    private Brand brand;
    private String licensePlate;
    private Integer yearManufacture;
    private Integer modelYear;
    private String nationalRegistryCode; // Renavam
    private BigDecimal fipeValueAtAcquisition;
    private BigDecimal acquisitionValue;
    private LocalDate acquisitionDate;
    private LocalDate availabilityDate;
    private BigDecimal mileageAtAcquisition;
    private Boolean auction;
    private BigDecimal fipeDepreciation;
    private String color;
    private String situation;
    private String fuelType;
    private BigDecimal odometer;

    public Vehicle() { }

    public Vehicle(Long id) {
        this.setId(id);
    }

    @Override
    public VehicleEntity toEntity() {
        VehicleEntity entity = new VehicleEntity();
        entity.setId(this.getId());
        entity.setLicensePlate(this.getLicensePlate().toUpperCase());
        entity.setYearManufacture(this.getYearManufacture());
        entity.setModelYear(this.getModelYear());
        entity.setNationalRegistryCode(this.getNationalRegistryCode());
        entity.setFipeValueAtAcquisition(this.getFipeValueAtAcquisition());
        entity.setAcquisitionValue(this.getAcquisitionValue());
        entity.setAcquisitionDate(this.getAcquisitionDate());
        entity.setAvailabilityDate(this.getAvailabilityDate());
        entity.setMileageAtAcquisition(this.getMileageAtAcquisition());
        entity.setAuction(this.getAuction());
        entity.setFipeDepreciation(this.getFipeDepreciation());
        entity.setColor(this.getColor().toUpperCase());
        entity.setActive(this.getActive());
        entity.setOdometer(this.getOdometer());

        Situation situation = Situation.toDescription(this.getSituation());
        entity.setSituation(situation.getDescription());

        FuelType fuelType = FuelType.toDescription(this.getFuelType());
        entity.setFuelType(fuelType.getDescription());
        return entity;
    }

    public static String shortName(VehicleEntity entity) {
        String brand = entity.getBrand().getName();
        String model = entity.getModel().getName();
        String [] modelParts = model.split(" ");
        return String.format(brand + " %s %s - %s",
                modelParts[0], modelParts[1], entity.getLicensePlate());
    }
}
