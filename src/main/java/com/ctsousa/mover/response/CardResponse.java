package com.ctsousa.mover.response;

import com.ctsousa.mover.enumeration.BankIcon;
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
    private Boolean active;

    public void setIcon(String nameIcon) {
        BankIcon icon = BankIcon.toName(nameIcon);
        this.icon = icon.name();
        this.imageIcon = icon.getImage();
        this.codeIcon = icon.getCode();
    }
}
