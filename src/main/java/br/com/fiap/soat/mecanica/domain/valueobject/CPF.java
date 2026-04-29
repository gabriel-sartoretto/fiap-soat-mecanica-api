package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.Getter;

@Getter
public class CPF {

    private final String value;

    public CPF(String value) {
        if (value == null || value.isBlank()) {
            throw new RegraNegocioException("CPF não pode ser vazio");
        }

        String cpf = value.replaceAll("\\D", "");

        if (cpf.length() != 11 || !isValidCPF(cpf)) {
            throw new RegraNegocioException("CPF inválido");
        }

        this.value = cpf;
    }

    private boolean isValidCPF(String cpf) {
        // evita sequências tipo 11111111111
        if (cpf.chars().distinct().count() == 1) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int dig1 = 11 - (soma % 11);
        dig1 = (dig1 >= 10) ? 0 : dig1;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int dig2 = 11 - (soma % 11);
        dig2 = (dig2 >= 10) ? 0 : dig2;

        return dig1 == (cpf.charAt(9) - '0') &&
                dig2 == (cpf.charAt(10) - '0');
    }
}
