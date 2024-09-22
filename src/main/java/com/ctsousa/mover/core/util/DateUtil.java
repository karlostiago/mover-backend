package com.ctsousa.mover.core.util;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public final class DateUtil {

    private static final Map<String, Integer> catalogOfMonths = new HashMap<>();

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
        if (isValidDateFormmatDate(dateStr, pattern)) return dateStr;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr);
        LocalDate localDate = zonedDateTime.toLocalDate();
        return localDate.format(formatter);
    }

    private static boolean isValidDateFormmatDate(String dateStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
