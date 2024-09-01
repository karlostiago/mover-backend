package com.ctsousa.mover.core.util;

import java.text.Normalizer;

public final class StringUtil {
    private static final String RG_REMOVER_CHARACTER_SPECIAL = "[^\\p{ASCII}]";

    private StringUtil() { }

    public static String removeLastPoint(String value) {
        if (value == null) return null;

        if (value.endsWith(".")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    public static String toUppercase(String value) {
        if (value == null || value.isEmpty()) return null;
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        String cleaned = normalized.replaceAll(RG_REMOVER_CHARACTER_SPECIAL, "");
        return cleaned.toUpperCase().trim();
    }
}
