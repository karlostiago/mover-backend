package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
public enum TypeMaintenance {

    SETUP(1, "SETUP"),
    MAINTENANCE(2, "MANUTENÇÃO");

    private final Integer code;
    private final String description;

    TypeMaintenance(int code, String description) {
        this.description = description;
        this.code = code;
    }

    public static TypeMaintenance fromQuery(String query) {
        for (TypeMaintenance typeMaintenance : TypeMaintenance.values()) {
            if (typeMaintenance.description.contains(toUppercase(query))) return typeMaintenance;
        }
        return null;
    }

    public static TypeMaintenance toDescription(String description) {
        for (TypeMaintenance typeMaintenance : TypeMaintenance.values()) {
            if (typeMaintenance.description.equals(description) || typeMaintenance.name().equalsIgnoreCase(description)) return typeMaintenance;
        }
        throw new NotificationException("Tipo manutenção não suportada :: " + description);
    }

    public static TypeMaintenance toCode(Integer code) {
        for (TypeMaintenance typeMaintenance : TypeMaintenance.values()) {
            if (typeMaintenance.code.equals(code)) return typeMaintenance;
        }
        throw new NotificationException("Código tipo manutenção não suportada :: " + code);
    }
}
