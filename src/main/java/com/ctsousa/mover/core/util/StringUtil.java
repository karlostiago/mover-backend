package com.ctsousa.mover.core.util;

import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.Set;

public final class StringUtil {
    private static final String RG_REMOVER_CHARACTER_SPECIAL = "[^\\p{ASCII}]";

    private StringUtil() {
    }

    public static String removeLastPoint(String value) {
        if (value == null) return null;

        if (value.endsWith(".")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    public static String toUppercase(String value) {
        if (value == null || value.isEmpty()) return null;
        return value.toUpperCase().trim();
    }

    public static String normalizer(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD);
        String cleaned = normalized.replaceAll(RG_REMOVER_CHARACTER_SPECIAL, "");
        return cleaned.toUpperCase().trim();
    }

    public static String removeDuplicateWords(String value) {
        String[] words = value.split("\\s+");
        Set<String> normalizedWords = new LinkedHashSet<>();
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            String standardized = normalizer(word);
            if (!normalizedWords.contains(standardized)) {
                normalizedWords.add(standardized);
                if (!result.isEmpty()) {
                    result.append(" ");
                }
                result.append(word);
            }
        }

        return toUppercase(result.toString());
    }
}
