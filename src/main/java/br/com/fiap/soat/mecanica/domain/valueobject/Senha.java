package br.com.fiap.soat.mecanica.domain.valueobject;

import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;

public class Senha {

    private final String valor;

    public Senha(String valor) {

        if (valor == null || valor.isBlank()) {
            throw new RegraNegocioException("Senha é obrigatória");
        }

        if (valor.length() < 8) {
            throw new RegraNegocioException("Senha deve ter no mínimo 8 caracteres");
        }

        if (valor.length() > 100) {
            throw new RegraNegocioException("Senha muito longa");
        }

        if (!valor.matches(".*\\d.*")) {
            throw new RegraNegocioException("Senha deve conter ao menos um número");
        }

        if (!valor.matches(".*[!@#$%^&*()_+\\-={}\\[\\]:;\"'<>,.?/].*")) {
            throw new RegraNegocioException("Senha deve conter ao menos um caractere especial");
        }

        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
