package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum Situation {

    IN_PROGRESS(1,"Em Progresso"),
    UNDER_ANALYSIS(2,"Em Analise"),
    FLEET(3,"Frota");

    private final Integer code;
    private final String description;

    Situation(int code, String description) {
        this.description = description.toUpperCase();
        this.code = code;
    }

    public static Situation toDescription(String description) {
        for (Situation situation : Situation.values()) {
            if (situation.description.equals(description)) return situation;
        }
        throw new NotificationException("Situacao não suportada :: " + description);
    }

    public static Situation toCode(Integer code) {
        for (Situation situation : Situation.values()) {
            if (situation.code.equals(code)) return situation;
        }
        throw new NotificationException("Código situacao não suportada :: " + code);
    }
}
