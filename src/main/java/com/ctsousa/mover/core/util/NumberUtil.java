package com.ctsousa.mover.core.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static BigDecimal parseMonetary(String value) {
        String regex = "^\\d{1,3}(\\.\\d{3})*,\\d{2}$|^\\d{1,3}(\\.\\d{3})*$|^\\d+,\\d{2}$|^\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) {
            String normalized = value.replace(".", "").replace(",", ".");
            if (!normalized.contains(".")) {
                normalized += ".00"; // Adicionar ".00" para valores inteiros
            }
            return new BigDecimal(normalized);
        }
        return null;
    }

    public static BigDecimal invertSignal(final BigDecimal value) {
        return value.multiply(BigDecimal.valueOf(-1));
    }

    public static String currencyFormatter(BigDecimal value, Locale locale) {
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        return format.format(value);
    }
}
