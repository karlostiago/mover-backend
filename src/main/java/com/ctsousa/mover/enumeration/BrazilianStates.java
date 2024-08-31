package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import com.ctsousa.mover.core.util.StringUtil;
import lombok.Getter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
public enum BrazilianStates {
    AC(1, "ACRE"),
    AL(2, "ALAGOAS"),
    AP(3, "AMAPÁ"),
    AM(4, "AMAZONAS"),
    BA(5, "BAHIA"),
    CE(6, "CEARÁ"),
    DF(7, "DISTRITO FEDERAL"),
    ES(8, "ESPÍRITO SANTO"),
    GO(9, "GOIÁS"),
    MA(10, "MARANHÃO"),
    MT(11, "MATO GROSSO"),
    MS(12, "MATO GROSSO DO SUL"),
    MG(13, "MINAS GERAIS"),
    PA(14, "PARÁ"),
    PB(15, "PARAÍBA"),
    PR(16, "PARANÁ"),
    PE(17, "PERNAMBUCO"),
    PI(18, "PIAUÍ"),
    RJ(19, "RIO DE JANEIRO"),
    RN(20, "RIO GRANDE DO NORTE"),
    RS(21, "RIO GRANDE DO SUL"),
    RO(22, "RONDÔNIA"),
    RR(23, "RORAIMA"),
    SC(24, "SANTA CATARINA"),
    SP(25, "SÃO PAULO"),
    SE(26, "SERGIPE"),
    TO(27, "TOCANTINS");

    private final Integer code;
    private final String description;

    BrazilianStates(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static BrazilianStates toCode(Integer code) {
        for (BrazilianStates state : BrazilianStates.values()) {
            if (state.code.equals(code)) return state;
        }
        throw new NotificationException("Código estado brasileiro, selecionado não suportado :: " + code);
    }

    public static BrazilianStates toName(String name) {
        for (BrazilianStates state : BrazilianStates.values()) {
            if (state.name().equals(toUppercase(name))) return state;
        }
        throw new NotificationException("Código estado brasileiro, selecionado não suportado :: " + name);
    }
}
