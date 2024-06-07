package com.ctsousa.mover.entity;

import com.ctsousa.mover.enumeration.CardType;
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
public class CardEntity extends AbstractEntity {

    private AccountEntity account;
    private CardType cardType;
    private String name;
    private BigDecimal limit;
    private LocalDate closingDate;
    private LocalDate dueDate;
    private String icon;
}
