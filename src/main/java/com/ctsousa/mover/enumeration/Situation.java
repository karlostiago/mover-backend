package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum Situation {

    IN_PROGRESS(1,"EM PROGRESSO"),
    UNDER_ANALYSIS(2,"EM ANALISE"),
    IN_FLEET(3,"EM FROTA"),
    SOLD(4, "VENDIDO"),
    TOTAL_LOSS(5, "PERDA TOTAL"),
    IN_ACQUISITION(6, "EM AQUISICAO"),
    CLOSED(7, "ENCERRADO"),
    ONGOING(8, "EM ANDAMENTO");

    private final Integer code;
    private final String description;

    Situation(int code, String description) {
        this.description = description.toUpperCase();
        this.code = code;
    }

    public static Situation toDescription(String description) {
        for (Situation situation : Situation.values()) {
            if (situation.description.equalsIgnoreCase(description)) return situation;
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
