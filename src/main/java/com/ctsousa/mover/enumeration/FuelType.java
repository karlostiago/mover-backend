package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum FuelType {
    ALCOHOL(1, "ALCOOL"),
    DIESEL(2, "DIESEL"),
    GASOLINE(3,"GASOLINA");

    private final Integer code;
    private final String description;

    FuelType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static FuelType toDescription(String description) {
        for (FuelType fuelType : FuelType.values()) {
            if (fuelType.description.equals(description)) return fuelType;
        }
        throw new NotificationException("Tipo combustivel não suportada :: " + description);
    }

    public static FuelType toCode(Integer code) {
        for (FuelType fuelType : FuelType.values()) {
            if (fuelType.code.equals(code)) return fuelType;
        }
        throw new NotificationException("Código tipo de combustivel não suportada :: " + code);
    }
}
