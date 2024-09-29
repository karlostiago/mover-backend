package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.CardEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Card extends DomainModel<CardEntity> {

    private String name;
    private Integer codeIcon;
    private String number;
    private BigDecimal InitialBalance;
    private Boolean caution;

    @Override
    public CardEntity toEntity() {
        CardEntity entity = new CardEntity();
//        entity.setId(this.getId());
//        entity.setCaution(this.getCaution());
//        entity.setActive(this.getActive());
//        entity.setName(toUppercase(this.getName()));
//        entity.setNumber(this.getNumber());
//        entity.setInitialBalance(this.getInitialBalance());
//
//        BankIcon icon = BankIcon.toCode(this.getCodeIcon());
//        entity.setIcon(icon.name());
//
//        String active = this.getActive() ? "SIM" : "NAO";
//        String caution = this.getCaution() ? "SIM" : "NAO";
//        String context = this.getName()
//                .concat(this.getCodeIcon().toString())
//                .concat(this.getNumber())
//                .concat(this.getInitialBalance().toString())
//                .concat(active)
//                .concat(caution);
//        entity.setHash(HashUtil.buildSHA256(context));

        return entity;
    }
}
