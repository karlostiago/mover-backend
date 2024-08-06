package com.ctsousa.mover.core.entity;

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
@Table(name = "tb_model")
public class ModelEntity extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "year_manufacture", nullable = false)
    private Integer yearManufacture;

    @Column(name = "year_model", nullable = false)
    private Integer yearModel;

    @JoinColumn(name = "brand_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private BrandEntity brand;
}
