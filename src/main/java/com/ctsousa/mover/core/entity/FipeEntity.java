package com.ctsousa.mover.core.entity;

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
    private String brand;
    private String model;
    private Integer modelYear;
    private String referenceMonth;
    private String fuel;
    private BigDecimal price;
}
