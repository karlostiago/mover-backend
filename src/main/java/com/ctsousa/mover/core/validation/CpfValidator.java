package com.ctsousa.mover.core.validation;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import org.apache.commons.lang3.StringUtils;

public class CpfValidator {

    public static boolean isValid(String value) {
        // Verifica se o valor é nulo ou vazio e se é um CPF válido
        return StringUtils.isBlank(value) || isCpf(removeFormatting(value));
    }

    /**
     * Valida e formata um CPF.
     *
     * @param cpf número de CPF a ser validado no formato 999.999.999-99 ou 99999999999
     * @return CPF formatado e validado
     * @throws NotificationException se o CPF for inválido ou não estiver no formato correto
     */
    public static String validateAndFormatCpf(String cpf) {

        if (StringUtils.isBlank(cpf)) {
            throw new NotificationException("Ops! Mensagem:\nDados do cliente não encontrados.");
        }
        cpf = removeFormatting(cpf);

        if (!isCpf(cpf)) {
            throw new NotificationException("Ops! Mensagem:\nFormato do CPF incorreto.");
        }

        return cpf;
    }

    /**
     * Valida um CPF.
     *
     * @param cpf número de CPF a ser validado, já formatado sem pontos e traços
     * @return true se o CPF é válido e false se não é válido
     */
    private static boolean isCpf(String cpf) {
        // Verifica se o CPF tem exatamente 11 dígitos
        if (cpf.length() != 11) return false;

        // Verifica se o CPF é uma sequência repetitiva de um único dígito
        if (cpf.chars().allMatch(ch -> ch == cpf.charAt(0))) return false;

        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(cpf.charAt(i));
        }

        // Calcula o primeiro dígito verificador
        int sum1 = digits[0] * 10 + digits[1] * 9 + digits[2] * 8 + digits[3] * 7 + digits[4] * 6
                + digits[5] * 5 + digits[6] * 4 + digits[7] * 3 + digits[8] * 2;
        int remainder1 = sum1 % 11;
        int digit1 = (remainder1 < 2) ? 0 : 11 - remainder1;

        // Calcula o segundo dígito verificador
        int sum2 = digits[0] * 11 + digits[1] * 10 + digits[2] * 9 + digits[3] * 8 + digits[4] * 7
                + digits[5] * 6 + digits[6] * 5 + digits[7] * 4 + digits[8] * 3 + digits[9] * 2;
        int remainder2 = sum2 % 11;
        int digit2 = (remainder2 < 2) ? 0 : 11 - remainder2;

        // Verifica se os dígitos verificadores calculados são iguais aos fornecidos
        return digit1 == digits[9] && digit2 == digits[10];
    }



    /**
     * Remove pontos e traços do CPF.
     *
     * @param cpf CPF que pode conter pontos e traços.
     * @return CPF sem formatação.
     */
    private static String removeFormatting(String cpf) {
        return cpf.replaceAll("[.-]", "");
    }
}
