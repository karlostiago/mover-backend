package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum TypeValueParameter {

    DECIMAL(1, "DECIMAL"),
    PERCENT(2, "PERCENTUAL"),
    DATE(3, "DATA"),
    CURRENCY(4, "MONETÁRIO"),
    NUMBER(5, "NÚMERO"),
    TEXT(6, "TEXTO");

    private final Integer code;
    private final String description;

    TypeValueParameter(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TypeValueParameter toDescription(String description) {
        for (TypeValueParameter configuration : TypeValueParameter.values()) {
            if (configuration.name().equalsIgnoreCase(description)
                    || configuration.description.equals(description)) return configuration;
        }
        throw new NotificationException("Configuração não suportada :: " + description);
    }

    public static TypeValueParameter toCode(Integer code) {
        for (TypeValueParameter configuration : TypeValueParameter.values()) {
            if (configuration.code.equals(code)) return configuration;
        }
        throw new NotificationException("Código configuração não suportada :: " + code);
    }
}
