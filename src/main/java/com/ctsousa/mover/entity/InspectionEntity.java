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
@Table(name = "inspection")
public class InspectionEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name", nullable = false)
    private ContractEntity contract;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "mileage", nullable = false)
    private Long mileage;

    @Column(name = "key", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean key;

    @Column(name = "radio", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean radio;

    @Column(name = "mokey", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean monkey;

    @Column(name = "triangle", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean triangle;

    @Column(name = "steppe", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean steppe;

    @Column(name = "glass", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean glass;

    @Column(name = "plate", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean plate;

    @Column(name = "rear_view_mirror", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean rearViewMirror;

    @Column(name = "ligthhouse", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean lighthouse;
}
