package com.ctsousa.mover.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "tb_mileage_control")
public class MileageControlEntity extends AbstractEntity {

    @JoinColumn(name = "vehicle_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleEntity vehicle;

    @Column(name = "period", nullable = false)
    private LocalDate period;

    @Column(name = "hour", nullable = false)
    private LocalTime hour;

    @Column(name = "odometer", nullable = false)
    private Double odometer;

    public MileageControlEntity() { }

    public MileageControlEntity(Long id) {
        super(id);
    }
}
