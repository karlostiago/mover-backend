package com.ctsousa.mover.entity;

import com.ctsousa.mover.enumeration.CardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_card")
public class CardEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "available_limit", nullable = false)
    private BigDecimal availableLimit;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "icon")
    private String icon;
}
