package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.util.HashUtil;
import com.ctsousa.mover.enumeration.BankIcon;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Account extends DomainModel<AccountEntity> {

    private String name;
    private Integer codeIcon;
    private String number;
    private BigDecimal InitialBalance;
    private Boolean caution;

    public Account() {}

    public Account(Long id) {
        setId(id);
    }

    @Override
    public AccountEntity toEntity() {
        AccountEntity entity = new AccountEntity();
        entity.setId(this.getId());
        entity.setCaution(this.getCaution());
        entity.setActive(this.getActive());
        entity.setName(toUppercase(this.getName()));
        entity.setNumber(this.getNumber());
        entity.setInitialBalance(this.getInitialBalance());
        entity.setAvailableBalance(this.getInitialBalance());

        BankIcon icon = BankIcon.toCode(this.getCodeIcon());
        entity.setIcon(icon.name());

        String active = this.getActive() ? "SIM" : "NAO";
        String caution = this.getCaution() ? "SIM" : "NAO";
        String context = this.getName()
                .concat(this.getCodeIcon().toString())
                .concat(this.getNumber())
                .concat(this.getInitialBalance().toString())
                .concat(active)
                .concat(caution);
        entity.setHash(HashUtil.buildSHA256(context));

        return entity;
    }
}
