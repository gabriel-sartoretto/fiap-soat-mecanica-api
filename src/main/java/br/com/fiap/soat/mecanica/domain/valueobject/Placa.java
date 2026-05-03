package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.Getter;

@Getter
public class Placa {

    private final String value;

    public Placa(String value) {
        if (value == null || value.isBlank()) {
            throw new RegraNegocioException("Placa é obrigatória");
        }

        String normalizada = value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();

        if (!isFormatoValido(normalizada)) {
            throw new RegraNegocioException("Placa inválida");
        }

        this.value = normalizada;
    }

    private boolean isFormatoValido(String placa) {
        return placa.matches("^[A-Z]{3}[0-9]{4}$") // antigo
                || placa.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$"); // mercosul
    }
}
