package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.Getter;

@Getter
public class Email {

    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new RegraNegocioException("Email não pode ser vazio");
        }

        String normalizado = value.trim().toLowerCase();

        if (!normalizado.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RegraNegocioException("Email inválido");
        }

        this.value = normalizado;
    }
}
