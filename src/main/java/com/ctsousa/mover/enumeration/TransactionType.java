package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum TransactionType {
    CREDIT(1, "CRÉDITO"),
    DEBIT(2, "DÉBITO");

    private final Integer code;
    private final String description;

    TransactionType(final int code, final String description) {
        this.code = code;
        this.description = description;
    }

    public static TransactionType toDescription(String description) {
        for (TransactionType type : TransactionType.values()) {
            if (type.description.equals(description) || type.name().equals(description)) return type;
        }
        throw new NotificationException("Tipo lançamento não suportada :: " + description);
    }

    public static TransactionType toCode(Integer code) {
        for (TransactionType type : TransactionType.values()) {
            if (type.code.equals(code)) return type;
        }
        throw new NotificationException("Código tipo lançamento não suportada :: " + code);
    }
}
