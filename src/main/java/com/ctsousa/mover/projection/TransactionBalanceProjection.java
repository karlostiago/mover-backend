package com.ctsousa.mover.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionBalanceProjection {
    LocalDate getDate();
    BigDecimal getBalance();
}
