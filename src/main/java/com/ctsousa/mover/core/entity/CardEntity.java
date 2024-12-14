package com.ctsousa.mover.core.entity;

import com.ctsousa.mover.enumeration.CardType;
import jakarta.persistence.*;
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

    @Column(name = "`limit`", nullable = false)
    private BigDecimal limit;

    @Column(name = "closing_day")
    private Integer closingDay;

    @Column(name = "due_date")
    private Integer dueDate;

    @Column(name = "icon")
    private String icon;

    @Column(name = "available_balance", nullable = false)
    private BigDecimal availableBalance;

    public CardEntity(Long id) {
        setId(id);
    }
}
