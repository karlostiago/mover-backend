package com.ctsousa.mover.core.validation;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import org.apache.commons.lang3.StringUtils;

public class CpfValidator {

    public static boolean isValid(String value) {
        return value == null || value.isEmpty() || isCpf(value);
    }

    /**
     * Valida um CPF.
     *
     * @param cpf número de CPF a ser validado no formato 999.999.999-99 ou 99999999999
     * @return true se o CPF é válido e false se não é válido
     */
    private static boolean isCpf(String cpf) {
        cpf = cpf.replace(".", "").replace("-", "");
        if (!cpf.matches("\\d{11}")) return false;

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cpf.charAt(i)));
        }

        int sum1 = digits[0]*10 + digits[1]*9 + digits[2]*8 + digits[3]*7 + digits[4]*6 + digits[5]*5
                + digits[6]*4 + digits[7]*3 + digits[8]*2;
        int remainder1 = sum1 % 11;
        int digit = (remainder1 < 2) ? 0 : 11 - remainder1;

        int sum2 = digits[0]*11 + digits[1]*10 + digits[2]*9 + digits[3]*8 + digits[4]*7 + digits[5]*6
                + digits[6]*5 + digits[7]*4 + digits[8]*3 + digits[9]*2;
        int remainder2 = sum2 % 11;
        int digit2 = (remainder2 < 2) ? 0 : 11 - remainder2;

        return digit == digits[9] && digit2 == digits[10];
    }

    /**
     * Formata o CPF removendo pontos e traços e valida se possui exatamente 11 dígitos.
     * usar no cadastro de cientes para formatar corretamente e enviar ao banco de dados sem ponto e traco,
     * evitando ocupar mais espaco no banco de dados da aplicacao.
     *
     * @param cpf CPF que pode conter pontos e traços.
     * @throws IllegalArgumentException se o CPF não tiver exatamente 11 dígitos após remover pontos e traços.
     */
    public static void formatCpf(String cpf) {
        if (StringUtils.isBlank(cpf)) {
            throw new NotificationException("CPF não fornecido corretamente");
        }

        // Remove pontos e traços
        cpf = cpf.replaceAll("[.-]", "");

        if (cpf.length() != 11) {
            throw new NotificationException("CPF deve ter exatamente 11 dígitos.");
        }
    }

}
