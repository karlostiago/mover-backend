package com.ctsousa.mover.entity;

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

    @Column(name = "ignition_key", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean ignitionKey;

    @Column(name = "radio", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean radio;

    @Column(name = "mokey", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean monkey;

    @Column(name = "triangle", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean triangle;

    @Column(name = "steppe", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean steppe;

    @Column(name = "glass", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean glass;

    @Column(name = "plate", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean plate;

    @Column(name = "rear_view_mirror", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean rearViewMirror;

    @Column(name = "ligthhouse", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Boolean lighthouse;
}
