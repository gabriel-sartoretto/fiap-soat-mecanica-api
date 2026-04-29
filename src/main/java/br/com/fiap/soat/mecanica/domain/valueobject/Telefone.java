package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.Getter;

@Getter
public class Telefone {

    private final String value;

    public Telefone(String value) {
        if (value == null || value.isBlank()) {
            throw new RegraNegocioException("Telefone não pode ser vazio");
        }

        String telefone = value.replaceAll("\\D", "");

        if (telefone.length() < 10 || telefone.length() > 11) {
            throw new RegraNegocioException("Telefone deve ter 10 ou 11 dígitos");
        }

        this.value = telefone;
    }
}
