package com.ctsousa.mover.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_fipe")
public class FipeEntity  extends AbstractEntity {
    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "model_year", nullable = false)
    private Integer modelYear;

    @Column(name = "reference_month", nullable = false)
    private String referenceMonth;

    @Column(name = "reference_year", nullable = false)
    private Integer referenceYear;

    @Column(name = "fuel", nullable = false)
    private String fuel;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "hash", nullable = false)
    private String hash;

    @Column(name = "price", nullable = false)
    private BigDecimal price;
}
