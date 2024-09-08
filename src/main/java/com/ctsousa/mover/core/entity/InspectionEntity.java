package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.InspectionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_inspection")
public class InspectionEntity extends AbstractEntity {

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, name = "contract")
    private String contract;

    @Column(nullable = false, name = "mileage")
    private BigDecimal mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_status", nullable = false)
    private InspectionStatus inspectionStatus;

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionQuestionEntity> questions = new ArrayList<>();

    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InspectionPhotoEntity> photos = new ArrayList<>();
}
