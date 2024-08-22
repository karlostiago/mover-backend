package com.ctsousa.mover.integration.fipe.parallelum.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class DateUtil {
    private DateUtil() { }

    public static String toMonthPtBr(LocalDate localDate) {
        if (localDate == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", new Locale("pt", "BR"));
        return localDate.format(formatter);
    }
}
