package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.InspectionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_inspection_photo")
public class InspectionPhotoEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "inspection_id")
    private InspectionEntity inspection;

    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_status", nullable = false)
    private InspectionStatus inspectionStatus;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private PhotoEntity photoEntity;
}