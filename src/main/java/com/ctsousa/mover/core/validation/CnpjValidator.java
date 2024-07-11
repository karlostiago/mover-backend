package com.ctsousa.mover.core.validation;

public class CnpjValidator {

    public static boolean isValid(String value) {
        return value == null || value.isEmpty() || isCnpj(value);
    }

    /**
     * Valida um CNPJ.
     *
     * @param cnpj número de CNPJ a ser validado no formato 99.999.999/9999-99 ou 99999999999999
     * @return true se o CNPJ é válido e false se não é válido
     */
    private static boolean isCnpj(String cnpj) {
        cnpj = cnpj.replace(".", "").replace("-", "").replace("/", "");
        if (!cnpj.matches("\\d{14}")) return false;

        int[] digits = new int[14];
        for (int i = 0; i < 14; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cnpj.charAt(i)));
        }

        int sum1 = digits[0]*5 + digits[1]*4 + digits[2]*3 + digits[3]*2 + digits[4]*9 + digits[5]*8
                + digits[6]*7 + digits[7]*6 + digits[8]*5 + digits[9]*4 + digits[10]*3 + digits[11]*2;
        int remainder1 = sum1 % 11;
        int digit1 = (remainder1 < 2) ? 0 : 11 - remainder1;

        int sum2 = digits[0]*6 + digits[1]*5 + digits[2]*4 + digits[3]*3 + digits[4]*2 + digits[5]*9
                + digits[6]*8 + digits[7]*7 + digits[8]*6 + digits[9]*5 + digits[10]*4 + digits[11]*3
                + digits[12]*2;
        int remainder2 = sum2 % 11;
        int digit2 = (remainder2 < 2) ? 0 : 11 - remainder2;

        return digit1 == digits[12] && digit2 == digits[13];
    }
}
