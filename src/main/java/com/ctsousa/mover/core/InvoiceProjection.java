package com.ctsousa.mover.core;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvoiceProjection {

    BigDecimal getValue();
    Long getPaid();
    LocalDate getDueDate();
}
