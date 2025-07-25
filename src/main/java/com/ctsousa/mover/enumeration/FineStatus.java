package com.ctsousa.mover.enumeration;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public enum FineStatus {

    PENDING("PENDENTE"),
    PAID("PAGO"),
    OVERDUE("EM ATRASO");

    private final String description;

    FineStatus(String description) {
        this.description = description;
    }

    public static String getDescriptionByStatus(LocalDate dueDate, Boolean paid) {
        if (Objects.nonNull(paid)) {
            return FineStatus.PAID.getDescription();
        }
        else if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            return FineStatus.OVERDUE.getDescription();
        } else {
            return FineStatus.PENDING.getDescription();
        }
    }
}
