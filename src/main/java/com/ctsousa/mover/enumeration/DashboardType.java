package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.entity.TransactionEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum DashboardType {
    OVERDUE_REVENUE {
        @Override
        public List<TransactionEntity> filter(List<TransactionEntity> entities) {
            LocalDate today = LocalDate.now();
            return entities.stream()
                    .filter(DashboardType::isIncome)
                    .filter(t -> t.getDueDate().isBefore(today) && !t.getPaid())
                    .toList();
        }
    },
    REALIZED_REVENUE {
        @Override
        public List<TransactionEntity> filter(List<TransactionEntity> entities) {
            return entities.stream()
                    .filter(DashboardType::isIncome)
                    .filter(TransactionEntity::getPaid)
                    .toList();
        }
    },
    PENDING_REVENUE {
        @Override
        public List<TransactionEntity> filter(List<TransactionEntity> entities) {
            LocalDate today = LocalDate.now();
            return entities.stream()
                    .filter(DashboardType::isIncome)
                    .filter(t -> !t.getDueDate().isBefore(today) && !t.getPaid())
                    .toList();
        }
    },
    GROSS_REVENUE {
        @Override
        public List<TransactionEntity> filter(List<TransactionEntity> entities) {
            return entities.stream()
                    .filter(DashboardType::isIncome)
                    .toList();
        }
    },
    OVERDUE_EXPENSE {
        @Override
        public List<TransactionEntity> filter(List<TransactionEntity> entities) {
            LocalDate today = LocalDate.now();
            return entities.stream()
                    .filter(DashboardType::isExpense)
                    .filter(t -> t.getDueDate().isBefore(today) && !t.getPaid())
                    .toList();
        }
    },
    REALIZED_EXPENSE {
        @Override
        public List<TransactionEntity> filter(List<TransactionEntity> entities) {
            return entities.stream()
                    .filter(DashboardType::isExpense)
                    .filter(TransactionEntity::getPaid)
                    .toList();
        }
    },
    PENDING_EXPENSE {
        @Override
        public List<TransactionEntity> filter(List<TransactionEntity> entities) {
            LocalDate today = LocalDate.now();
            return entities.stream()
                    .filter(DashboardType::isExpense)
                    .filter(t -> !t.getDueDate().isBefore(today) && !t.getPaid())
                    .toList();
        }
    },
    GROSS_EXPENSE {
        @Override
        public List<TransactionEntity> filter(List<TransactionEntity> entities) {
            return entities.stream()
                    .filter(DashboardType::isExpense)
                    .toList();
        }
    };

    public abstract List<TransactionEntity> filter(List<TransactionEntity> entities);

    public static Optional<DashboardType> from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst();
    }

    private static boolean isExpense(TransactionEntity entity) {
        return TypeCategory.EXPENSE.name().equalsIgnoreCase(entity.getCategoryType())
                || TypeCategory.INVESTMENT.name().equalsIgnoreCase(entity.getCategoryType());
    }

    private static boolean isIncome(TransactionEntity entity) {
        return TypeCategory.INCOME.name().equalsIgnoreCase(entity.getCategoryType())
                || TypeCategory.CORPORATE_CAPITAL.name().equalsIgnoreCase(entity.getCategoryType());
    }
}
