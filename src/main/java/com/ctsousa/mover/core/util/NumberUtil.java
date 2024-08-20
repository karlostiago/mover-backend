package com.ctsousa.mover.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public final class NumberUtil {
    private NumberUtil() { }

    public static BigDecimal toBigDecimal(String value) {
        if (value == null || value.isEmpty()) return BigDecimal.ZERO;

        try {
            String cleanValue = value.replaceAll("[^\\d,]", "");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');

            DecimalFormat decimalFormat = new DecimalFormat("###,###.##", symbols);
            decimalFormat.setParseBigDecimal(true);

            return (BigDecimal) decimalFormat.parse(cleanValue);
        } catch (ParseException e) {
            return BigDecimal.ZERO;
        }
    }
}
