package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.Data;

@Data
public class CNPJ {

    private final String value;

    public CNPJ(String value) {
        if (value == null || value.isBlank()) {
            throw new RegraNegocioException("CNPJ não pode ser vazio");
        }

        String cnpj = value
                .replaceAll("[^A-Za-z0-9]", "")
                .toUpperCase();

        if (!cnpj.matches("^[A-Z0-9]{12}\\d{2}$") || !isValidCNPJ(cnpj)) {
            throw new RegraNegocioException("CNPJ inválido");
        }

        this.value = cnpj;
    }

    private int charToInt(char c) {
        if (Character.isDigit(c)) return c - '0';
        return c - 'A' + 10;
    }

    private boolean isValidCNPJ(String cnpj) {
        if (cnpj.chars().distinct().count() == 1) return false;

        int[] peso1 = {5,4,3,2,9,8,7,6,5,4,3,2};
        int[] peso2 = {6,5,4,3,2,9,8,7,6,5,4,3,2};

        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += charToInt(cnpj.charAt(i)) * peso1[i];
        }

        int dig1 = soma % 11;
        dig1 = (dig1 < 2) ? 0 : 11 - dig1;

        soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += charToInt(cnpj.charAt(i)) * peso2[i];
        }

        int dig2 = soma % 11;
        dig2 = (dig2 < 2) ? 0 : 11 - dig2;

        return dig1 == (cnpj.charAt(12) - '0') &&
                dig2 == (cnpj.charAt(13) - '0');
    }
}