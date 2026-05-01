package br.com.fiap.soat.mecanica.adapters.in.web.servico.dto;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;

import java.util.UUID;

public record ServicoResponse(
        UUID id,
        StatusRecursoEnum statusRecursoEnum,
        String nome,
        String descricao
) {
}
