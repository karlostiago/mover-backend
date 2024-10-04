package com.ctsousa.mover.request;

import com.ctsousa.mover.core.annotation.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardRequest {
    private Long id;

    @NotEmpty(message = "Nome não pode ser vázio")
    private String name;

    @NotEmpty(message = "Limite não pode ser vázio")
    private BigDecimal limit;

    @NotEmpty(message = "Dia do fechamento não pode ser vázio")
    private Integer closingDay;

    @NotEmpty(message = "Dia de vencimento não pode ser vázio")
    private Integer dueDate;

    @NotEmpty(message = "Ícone não pode ser vázio")
    private Integer codeIcon;

    @NotEmpty(message = "Tipo não pode ser vázio")
    private String type;

    @NotEmpty(message = "Conta não pode ser vázio")
    private Long accountId;

    private Boolean active;
}
