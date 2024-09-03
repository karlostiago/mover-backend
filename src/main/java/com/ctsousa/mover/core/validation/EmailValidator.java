package com.ctsousa.mover.core.validation;

import com.ctsousa.mover.core.exception.notification.NotificationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class EmailValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private EmailValidator() { }

    public static String ensureEmailEndsWithCom(String email) {
        if (email != null && !email.isEmpty()) {
            String trimmedEmail = email.trim();
            if (!trimmedEmail.toLowerCase().endsWith(".com")) {
                    trimmedEmail += ".com";
            }
            return trimmedEmail;
        }
        return email;
    }

    public static void valid(String email) {
        if (email == null) throw new NotificationException("Email inválido.");

        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new NotificationException("Email inválido.");
        }
    }
}
