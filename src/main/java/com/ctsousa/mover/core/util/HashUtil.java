package com.ctsousa.mover.core.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtil {

    private HashUtil() { }

    public static String buildSHA256(String context) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte [] bytes = digest.digest(context.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexstr = new StringBuilder(2 * bytes.length);
            for (byte b : bytes) {
                String hash = Integer.toHexString(0xff & b);
                if (hash.length() == 1) {
                    hexstr.append('0');
                }
                hexstr.append(hash);
            }
            return hexstr.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
