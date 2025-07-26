package com.ctsousa.mover.enumeration;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public enum FineStatus {

    PENDING("Pendente"),
    PAID("Pago"),
    OVERDUE("Em atraso");

    private final String description;

    FineStatus(String description) {
        this.description = description;
    }

    public static String getDescriptionByStatus(LocalDate dueDate, Boolean paid) {
        if (Objects.nonNull(paid) && paid) {
            return FineStatus.PAID.getDescription();
        }
        else if (Objects.nonNull(dueDate) && dueDate.isBefore(LocalDate.now())) {
            return FineStatus.OVERDUE.getDescription();
        } else {
            return FineStatus.PENDING.getDescription();
        }
    }
}
