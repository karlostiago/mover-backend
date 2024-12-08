package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.core.annotation.DateFormat;
import com.ctsousa.mover.enumeration.InspectionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_inspection")

public class InspectionEntity extends AbstractEntity {

    @Column(nullable = false)
    @DateFormat
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    @JsonBackReference
    private ContractEntity contract;

    @Column(nullable = false, name = "mileage")
    private BigDecimal mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_status", nullable = false)
    private InspectionStatus inspectionStatus;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<InspectionQuestionEntity> questions = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<InspectionPhotoEntity> photos = new HashSet<>();

}
