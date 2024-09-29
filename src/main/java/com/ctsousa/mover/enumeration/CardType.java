package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;

public enum CardType {

    CREDIT(1, "CRÉDITO"),
    DEBIT(2, "DÉBITO");

    private final Integer code;
    private final String description;

    CardType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static CardType toDescription(String description) {
        for (CardType cardType : CardType.values()) {
            if (cardType.description.equals(description)) return cardType;
        }
        throw new NotificationException("Tipo cartão não suportada :: " + description);
    }

    public static CardType toCode(Integer code) {
        for (CardType cardType : CardType.values()) {
            if (cardType.code.equals(code)) return cardType;
        }
        throw new NotificationException("Código tipo de cartão não suportada :: " + code);
    }
}
