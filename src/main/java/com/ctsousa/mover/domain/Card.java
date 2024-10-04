package com.ctsousa.mover.domain;

import com.ctsousa.mover.core.entity.AccountEntity;
import com.ctsousa.mover.core.entity.CardEntity;
import com.ctsousa.mover.core.util.StringUtil;
import com.ctsousa.mover.enumeration.BankIcon;
import com.ctsousa.mover.enumeration.CardType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
@Setter
public class Card extends DomainModel<CardEntity> {

    private String name;
    private BigDecimal limit;
    private Integer closingDay;
    private Integer dueDate;
    private Integer codeIcon;
    private String type;
    private Long accountId;

    @Override
    public CardEntity toEntity() {
        CardEntity entity = new CardEntity();
        entity.setId(this.getId());
        entity.setName(toUppercase(this.getName()));
        entity.setLimit(this.getLimit());
        entity.setClosingDay(this.getClosingDay());
        entity.setDueDate(this.getDueDate());

        AccountEntity account = new AccountEntity(this.getAccountId());
        entity.setAccount(account);

        entity.setIcon(BankIcon.toCode(this.getCodeIcon()).name());
        entity.setCardType(CardType.toDescription(this.getType()));

        return entity;
    }
}
