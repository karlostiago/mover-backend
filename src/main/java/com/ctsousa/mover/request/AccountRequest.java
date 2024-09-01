package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {
    private Long id;

    @NotEmpty(message = "Campo descrição da conta não pode ser vázio")
    private String name;

    @NotEmpty(message = "Campo número da conta não pode ser vázio")
    private String number;

    @NotEmpty(message = "Campo ícone não pode ser vázio")
    private Integer codeIcon;

    private BigDecimal InitialBalance;

    private Boolean caution;

    private Boolean active;
}
