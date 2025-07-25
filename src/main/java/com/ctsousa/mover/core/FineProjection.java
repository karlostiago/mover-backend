package com.ctsousa.mover.core;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface FineProjection {
    Long getId();
    String getDescription();
    LocalDateTime getDateTimeOfCommitment();
    LocalDate getDueDate();
    Long getVehicleId();
    BigDecimal getValue();
    BigDecimal getPaid();
    String getLicensePlate();
    String getModel();
    String getBrand();
}
