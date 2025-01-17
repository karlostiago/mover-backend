package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
public enum TypeCategory {

    INCOME(1, "RECEITA", TransactionType.CREDIT),
    EXPENSE(2, "DESPESA", TransactionType.DEBIT),
    INVESTMENT(3, "INVESTIMENTO", TransactionType.DEBIT),
    TRANSFER(4, "TRANSFERÊNCIA", TransactionType.DEBIT),
    CORPORATE_CAPITAL(5, "CAPITAL SOCIETÁRIO", TransactionType.CREDIT);

    private final Integer code;
    private final String description;
    private final TransactionType transactionType;

    TypeCategory(int code, String description, TransactionType transactionType) {
        this.transactionType = transactionType;
        this.description = description;
        this.code = code;
    }

    public static TypeCategory fromQuery(String query) {
        for (TypeCategory typeCategory : TypeCategory.values()) {
            if (typeCategory.description.contains(toUppercase(query))) return typeCategory;
        }
        return null;
    }

    public static TypeCategory toDescription(String description) {
        for (TypeCategory typeCategory : TypeCategory.values()) {
            if (typeCategory.name().equals(description) || typeCategory.description.equals(description)) return typeCategory;
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
