package br.com.fiap.soat.mecanica.adapters.in.web.servico.dto;

import jakarta.validation.constraints.NotBlank;

public record ServicoIncluirRequest(

        @NotBlank(message = "O nome não pode ser nulo nem vazio")
        String nome,

        String descricao
) {
}
