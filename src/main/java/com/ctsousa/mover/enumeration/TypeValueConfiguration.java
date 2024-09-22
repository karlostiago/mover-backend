package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum TypeValueConfiguration {

    DECIMAL(1, "DECIMAL"),
    PERCENT(2, "PERCENTUAL"),
    DATE(3, "DATA"),
    CURRENCY(4, "MONETÁRIO"),
    NUMBER(5, "NÚMERO"),
    TEXT(6, "TEXTO");

    private final Integer code;
    private final String description;

    TypeValueConfiguration(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TypeValueConfiguration toDescription(String description) {
        for (TypeValueConfiguration configuration : TypeValueConfiguration.values()) {
            if (configuration.name().equalsIgnoreCase(description)
                    || configuration.description.equals(description)) return configuration;
        }
        throw new NotificationException("Configuração não suportada :: " + description);
    }

    public static TypeValueConfiguration toCode(Integer code) {
        for (TypeValueConfiguration configuration : TypeValueConfiguration.values()) {
            if (configuration.code.equals(code)) return configuration;
        }
        throw new NotificationException("Código configuração não suportada :: " + code);
    }
}
