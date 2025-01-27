package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.BankIcon;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountResponse {
    private Long id;
    private String name;
    private String number;
    private String icon;
    private String imageIcon;
    private Integer codeIcon;
    private BigDecimal initialBalance;
    private Boolean caution;
    private Boolean active;

    public void setIcon(String nameIcon) {
        BankIcon icon = BankIcon.toName(nameIcon);
        this.icon = icon.name();
        this.imageIcon = icon.getUrlImage();
        this.codeIcon = icon.getCode();
    }
}
