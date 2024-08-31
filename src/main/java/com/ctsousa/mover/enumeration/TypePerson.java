package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum TypePerson {
    NATURAL_PERSON(1, "PESSOA FÍSICA");
//    LEGAL_PERSON(2, "PESSOA JÚRIDICA");

    private final Integer code;
    private final String description;

    TypePerson(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static TypePerson toDescription(String description) {
        for (TypePerson fuelType : TypePerson.values()) {
            if (fuelType.description.equals(description)) return fuelType;
        }
        throw new NotificationException("Tipo pessoa não suportada :: " + description);
    }

    public static TypePerson toCode(Integer code) {
        for (TypePerson fuelType : TypePerson.values()) {
            if (fuelType.code.equals(code)) return fuelType;
        }
        throw new NotificationException("Código tipo pessoa não suportada :: " + code);
    }
}
