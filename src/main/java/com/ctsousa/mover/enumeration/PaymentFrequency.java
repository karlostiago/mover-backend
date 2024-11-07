package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
public enum PaymentFrequency {
    WEEKLY(1, "SEMANAL"),
    BIWEEKLY(2, "QUINZENAL"),
    MONTHLY(3, "MENSAL");

    private final Integer code;
    private final String description;

    PaymentFrequency(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static PaymentFrequency toDescription(String description) {
        for (PaymentFrequency paymentFrequency : PaymentFrequency.values()) {
            if (paymentFrequency.description.equals(description)
                    || toUppercase(paymentFrequency.name()).equals(toUppercase(description))) return paymentFrequency;
        }
        throw new NotificationException("Frequência de pagamento não suportada :: " + description);
    }

    public static PaymentFrequency toCode(Integer code) {
        for (PaymentFrequency paymentFrequency : PaymentFrequency.values()) {
            if (paymentFrequency.code.equals(code)) return paymentFrequency;
        }
        throw new NotificationException("Código frequência de pagamento não suportada :: " + code);
    }
}
