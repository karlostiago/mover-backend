package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
public enum DayOfWeek {
    SUNDAY(1, "DOMINGO"),
    MONDAY(2, "SEGUNDA-FEIRA"),
    TUESDAY(3, "TERÇA-FEIRA"),
    WEDNESDAY(4, "QUARTA-FEIRA"),
    THURSDAY(5, "QUINTA-FEIRA"),
    FRIDAY(6, "SEXTA-FEIRA"),
    SATURDAY(7, "SÁBADO");

    private final Integer code;
    private final String description;

    DayOfWeek(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static DayOfWeek toDescription(String description) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (dayOfWeek.description.equals(description)
                    || toUppercase(dayOfWeek.name()).equals(toUppercase(description))) return dayOfWeek;
        }
        throw new NotificationException("Dia da semana não suportada :: " + description);
    }

    public static DayOfWeek toCode(Integer code) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (dayOfWeek.code.equals(code)) return dayOfWeek;
        }
        throw new NotificationException("Código dia da semana não suportada :: " + code);
    }
}
