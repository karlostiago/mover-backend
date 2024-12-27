package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

import static com.ctsousa.mover.core.util.StringUtil.toUppercase;

@Getter
public enum PaymentFrequency {
    WEEKLY(1, "SEMANAL") {
        @Override
        public long days() {
            return 7L;
        }
    },
    BIWEEKLY(2, "QUINZENAL") {
        @Override
        public long days() {
            return 15L;
        }
    },
    MONTHLY(3, "MENSAL") {
        @Override
        public long days() {
            return 30L;
        }
    },
    DAILY(4, "DIÁRIO") {
        @Override
        public long days() {
            return 1L;
        }
    },
    BIMONTHLY(5, "BIMESTRAL") {
        @Override
        public long days() {
            return 60L;
        }
    },
    QUARTERLY(6, "TRIMESTRAL") {
        @Override
        public long days() {
            return 90L;
        }
    },
    SEMIANNUAL(7, "SIMESTRAL") {
        @Override
        public long days() {
            return 180L;
        }
    },
    ANNUAL(8, "ANUAL") {
        @Override
        public long days() {
            return 360L;
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

    public abstract long days();
}
