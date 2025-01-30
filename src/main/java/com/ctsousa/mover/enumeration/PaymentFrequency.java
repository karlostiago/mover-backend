package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

import java.time.LocalDate;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
public enum PaymentFrequency {
    WEEKLY(1, "SEMANAL") {
        @Override
        public LocalDate nextDate(LocalDate date) {
            return date.plusDays(7);
        }
    },
    BIWEEKLY(2, "QUINZENAL") {
        @Override
        public LocalDate nextDate(LocalDate date) {
            return date.plusDays(15);
        }
    },
    MONTHLY(3, "MENSAL") {
        @Override
        public LocalDate nextDate(LocalDate date) {
            return date.plusMonths(1);
        }
    },
    DAILY(4, "DIÁRIO") {
        @Override
        public LocalDate nextDate(LocalDate date) {
            return date.plusDays(1);
        }
    },
    BIMONTHLY(5, "BIMESTRAL") {
        @Override
        public LocalDate nextDate(LocalDate date) {
            return date.plusMonths(2);
        }
    },
    QUARTERLY(6, "TRIMESTRAL") {
        @Override
        public LocalDate nextDate(LocalDate date) {
            return date.plusMonths(3);
        }
    },
    SEMIANNUAL(7, "SIMESTRAL") {
        @Override
        public LocalDate nextDate(LocalDate date) {
            return date.plusMonths(6);
        }
    },
    ANNUAL(8, "ANUAL") {
        @Override
        public LocalDate nextDate(LocalDate date) {
            return  date.plusYears(1);
        }
    };

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

    public abstract LocalDate nextDate(LocalDate date);
}
