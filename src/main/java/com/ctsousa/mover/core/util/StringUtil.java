package com.ctsousa.mover.core.util;

public final class StringUtil {
    private StringUtil() { }

    public static String removeLastPoint(String value) {
        if (value == null) return null;

        if (value.endsWith(".")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }
}
