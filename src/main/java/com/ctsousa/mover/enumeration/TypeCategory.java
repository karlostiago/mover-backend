package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum TypeCategory {

    INCOME(1, "RECEITA"),
    EXPENSE(2, "DESPESA"),
    INVESTMENT(3, "INVESTIMENTO");

    private final Integer code;
    private final String description;

    TypeCategory(int code, String description) {
        this.description = description;
        this.code = code;
    }

    public static TypeCategory toDescription(String description) {
        for (TypeCategory typeCategory : TypeCategory.values()) {
            if (typeCategory.description.equals(description)) return typeCategory;
        }
        throw new NotificationException("Tipo categoria não suportada :: " + description);
    }

    public static TypeCategory toCode(Integer code) {
        for (TypeCategory typeCategory : TypeCategory.values()) {
            if (typeCategory.code.equals(code)) return typeCategory;
        }
        throw new NotificationException("Código tipo categoria não suportada :: " + code);
    }
}