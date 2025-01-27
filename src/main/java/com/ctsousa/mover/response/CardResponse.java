package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.BankIcon;
import com.ctsousa.mover.enumeration.CardType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardResponse {
    private Long id;
    private String name;
    private String icon;
    private String imageIcon;
    private Integer codeIcon;
    private BigDecimal limit;
    private Integer dueDate;
    private Integer closingDay;
    private Long accountId;
    private String cardType;
    private Boolean active;

    public void setIcon(String nameIcon) {
        BankIcon icon = BankIcon.toName(nameIcon);
        this.icon = icon.name();
        this.imageIcon = icon.getUrlImage();
        this.codeIcon = icon.getCode();
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType.getDescription();
    }
}
