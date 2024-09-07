package com.ctsousa.mover.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
public class InspectionPhoto extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "inspection_id")
    private Inspection inspection;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
}
