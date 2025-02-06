package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum PermissionType {

    PROFILE(1, "PERFIL"),
    USER(2, "USUÁRIO");

    private final Integer code;
    private final String description;

    PermissionType(int code, String description) {
        this.description = description.toUpperCase();
        this.code = code;
    }

    public static PermissionType toDescription(String description) {
        for (PermissionType type : PermissionType.values()) {
            if (type.description.equals(description)) return type;
        }
        throw new NotificationException("Tipo permissão não suportada :: " + description);
    }

    public static PermissionType toCode(Integer code) {
        for (PermissionType type : PermissionType.values()) {
            if (type.code.equals(code)) return type;
        }
        throw new NotificationException("Código tipo permissão não suportada :: " + code);
    }
}
