package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum QuestionnaireType {

    DELIVERY(1,"Entrega"),
    PICKUP(2,"Coleta"),
    SELF_INSPECTION(3,"Auto-vistoria");

    private final Integer code;
    private final String label;

    QuestionnaireType(Integer code,String label) {
        this.code = code;
        this.label = label;
    }

    public static QuestionnaireType toCode(Integer code)  {
        for (QuestionnaireType questionnaireType : QuestionnaireType.values()) {
            if (questionnaireType.getCode().equals(code)) {
                return questionnaireType;
            }
        }
        throw new NotificationException("Tipo do código não suportado.");
    }

    public static QuestionnaireType toLabel(String label)  {
        for (QuestionnaireType questionnaireType : QuestionnaireType.values()) {
            if (questionnaireType.getLabel().equals(label)) {
                return questionnaireType;
            }
        }
        throw new NotificationException("Tipo do questionário não Suportado.");
    }
}