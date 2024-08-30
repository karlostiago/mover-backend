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
    private String accountNumber;
    private String icon;
    private BigDecimal InitialBalance;
    private Boolean caution;
    private Boolean active;

    public void setIcon(String icon) {
        this.icon = BankIcon.toName(icon)
                .getUrlImage();
    }
}
