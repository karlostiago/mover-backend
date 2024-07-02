package com.ctsousa.mover.core.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_inspection")
public class InspectionEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private ContractEntity contract;

    @Column(name = "date_create", nullable = false)
    private LocalDate date;

    @Column(name = "mileage", nullable = false)
    private Long mileage;

    @Column(name = "ignition_key", nullable = false)
    private Boolean ignitionKey;

    @Column(name = "radio", nullable = false)
    private Boolean radio;

    @Column(name = "mokey", nullable = false)
    private Boolean monkey;

    @Column(name = "triangle", nullable = false)
    private Boolean triangle;

    @Column(name = "steppe", nullable = false)
    private Boolean steppe;

    @Column(name = "glass", nullable = false)
    private Boolean glass;

    @Column(name = "plate", nullable = false)
    private Boolean plate;

    @Column(name = "rear_view_mirror", nullable = false)
    private Boolean rearViewMirror;

    @Column(name = "ligthhouse", nullable = false)
    private Boolean lighthouse;
}
