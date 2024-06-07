package com.ctsousa.mover.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationEntity extends AbstractEntity {

    private BigDecimal fipeDevaluation;
    private BigDecimal fipeAuctionDevaluation;
}
