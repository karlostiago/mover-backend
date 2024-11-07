package com.ctsousa.mover.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class VehicleResponse {
    private Long id;
    private Long brandId;
    private String brandName;
    private Long modelId;
    private String modelName;
    private String licensePlate;
    private Integer yearManufacture;
    private Integer modelYear;
    private String renavam;
    private BigDecimal fipeValueAtAcquisition;
    private BigDecimal acquisitionValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate acquisitionDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate availabilityDate;
    private BigDecimal mileageAtAcquisition;
    private Boolean auction;
    private BigDecimal fipeDepreciation;
    private String color;
    private String situation;
    private String fuelType;
    private String fullname;
    private Boolean active;

    public String getFullname() {
        return this.brandName + " - " + this.modelName + " - " + this.licensePlate;
    }
}
