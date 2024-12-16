package com.ctsousa.mover.core.util;

import com.ctsousa.mover.core.exception.notification.NotificationException;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DateUtil {

    private static final Map<String, Integer> catalogOfMonths = new HashMap<>();

    private static final int minYear;
    private static final int maxYear;
    private static final int minMonth;
    private static final int maxMonth;
    private static final int minDay;
    private static final int maxDay;

    private DateUtil() {

    }

    static {
        catalogOfMonths.put("JAN", 1);
        catalogOfMonths.put("FEB", 2);
        catalogOfMonths.put("MAR", 3);
        catalogOfMonths.put("APR", 4);
        catalogOfMonths.put("MAY", 5);
        catalogOfMonths.put("JUN", 6);
        catalogOfMonths.put("JUL", 7);
        catalogOfMonths.put("AUG", 8);
        catalogOfMonths.put("SEP", 9);
        catalogOfMonths.put("OCT", 10);
        catalogOfMonths.put("NOV", 11);
        catalogOfMonths.put("DEC", 12);

        maxYear = 3000;
        minYear = 1900;
        minMonth = 1;
        maxMonth = 12;
        minDay = 1;
        maxDay = 31;
    }

    public static LocalDate toLocalDateWithGMT(String dateStr) {
        String date = dateStr.substring(0, dateStr.indexOf("GMT") - 1).trim();
        String [] splitDate = date.split(" ");
        int day = Integer.parseInt(splitDate[2]);
        int month = catalogOfMonths.get(splitDate[1].toUpperCase());
        int year = Integer.parseInt(splitDate[3]);

        return LocalDate.of(year, month, day);
    }

    public static String toDateFromBr(String dateStr) {
        String pattern = "dd/MM/yyyy";
        if (isValidDateFormmatter(dateStr, pattern)) return dateStr;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr);
        LocalDate localDate = zonedDateTime.toLocalDate();

        if (!isValidDate(localDate)) {
            throw new NotificationException("Data inválida.");
        }
        
        return localDate.format(formatter);
    }

    public static boolean isValidDate(String date) {
        for (DateTimeFormatter formatter : formatters()) {
            try {
                LocalDate.parse(date, formatter);
                return true;
            } catch (DateTimeParseException e) {
                // Ignora e tenta o próximo formato.
            }
        }
        return false;
    }

    public static LocalDate parseToLocalDate(String date) {
        for (DateTimeFormatter formatter : formatters()) {
            try {
                return LocalDate.parse(date, formatter);
            } catch (DateTimeParseException e) {
                // Ignora e tenta o próximo formato.
            }
        }
        return null;
    }

    public static boolean isValidDate(LocalDate localDate) {
        return isValidYear(localDate) && isValidMonth(localDate) && isValidDay(localDate);
    }

    private static List<DateTimeFormatter> formatters() {
        return Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("ddMMyyyy"),
                DateTimeFormatter.ofPattern("yyyyMMdd")
        );
    }
    
    private static boolean isValidDateFormmatter(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isValidYear(LocalDate localDate) {
        int year = localDate.getYear();
        return  year >= minYear && year <= maxYear;
    }

    private static boolean isValidMonth(LocalDate localDate) {
        int month = localDate.getMonth().getValue();
        return month >= minMonth && month <= maxMonth;
    }

    private static boolean isValidDay(LocalDate localDate) {
        int day = localDate.getDayOfMonth();
        return day >= minDay && day <= maxDay;
    }
}
