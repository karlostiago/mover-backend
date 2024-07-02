package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.Situation;
import jakarta.persistence.*;
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
@Entity
@Table(name = "tb_car")
public class CarEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private ModelEntity model;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "national_vehicle_registry", nullable = false)
    private String nationalVehicleRegistry;

    @Column(name = "fipe_acquisition_date", nullable = false)
    private LocalDate fipeAcquisitionDate;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "acquisition_date", nullable = false)
    private LocalDate acquisitionDate;

    @Column(name = "ready_date")
    private LocalDate readyDate;

    @Column(name = "km_aquisition", nullable = false)
    private BigDecimal kmAquisition;

    @Column(name = "auction", nullable = false)
    private Boolean auction = Boolean.FALSE;

    @Column(name = "fipe_devaluation")
    private BigDecimal fipeDevaluation;

    @Enumerated(EnumType.STRING)
    @Column(name = "situation", nullable = false)
    private Situation situation;
}
