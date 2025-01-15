package com.ctsousa.mover.enumeration;

import lombok.Getter;

@Getter
public enum Menu {

    MY_CLIENTS( 1,"Menu de clientes"),
    MY_FLEETS( 2,"Menu de frotas"),
    MY_MONEY( 3,"Menu meu dinheiro"),
    CONFIGURATION( 4,"Menu de configurações"),
    SECURITY( 5,"Menu segurança");

    private final int code;
    private final String description;

    Menu(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
