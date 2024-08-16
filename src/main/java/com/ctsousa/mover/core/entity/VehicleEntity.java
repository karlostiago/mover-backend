package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.Situation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_vehicle")
public class VehicleEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private ModelEntity model;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "year_manufacture", nullable = false)
    private Integer yearManufacture;

    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @Column(name = "renavam", nullable = false)
    private String renavam;

    @Column(name = "fipe_value_at_acquisition", nullable = false)
    private BigDecimal fipeValueAtAcquisition;

    @Column(name = "value_acquisition", nullable = false)
    private BigDecimal acquisitionValue;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDate acquisitionDate;

    @Column(name = "availability_date", nullable = false)
    private LocalDate availabilityDate;

    @Column(name = "mileage_at_acquisition", nullable = false)
    private BigDecimal mileageAtAcquisition;

    @Column(name = "auction", nullable = false)
    private Boolean auction;

    @Column(name = "fipe_depreciation")
    private BigDecimal fipeDepreciation;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "situation", nullable = false)
    private String situation;

    public void setSituation(String situation) {
        this.situation = Objects.requireNonNull(Situation.toDescription(situation))
                .getDescription();
    }
}
