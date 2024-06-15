package com.ctsousa.mover.entity;

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
@Table(name = "configuration")
public class ConfigurationEntity extends AbstractEntity {

    @Column(name = "fipe_devaluation", nullable = false)
    private BigDecimal fipeDevaluation;

    @Column(name = "fipe_auction_devaluation", nullable = false)
    private BigDecimal fipeAuctionDevaluation;
}
