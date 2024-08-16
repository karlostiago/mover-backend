package com.ctsousa.mover.enumeration;

import lombok.Getter;

@Getter
public enum Situation {

    IN_PROGRESS(1,"Em Progresso"),
    UNDER_ANALYSIS(2,"Em Analise"),
    FLEET(3,"Frota");

    private final int code;
    private final String description;

    Situation(int code, String description) {
        this.description = description;
        this.code = code;
    }

    public static Situation toCode(int code) {
        for (Situation situation : Situation.values()) {
            if (situation.code == code) return situation;
        }
        return null;
    }
}
