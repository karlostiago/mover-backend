package com.ctsousa.mover.core.validation;

public class EmailValidator {

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
}
