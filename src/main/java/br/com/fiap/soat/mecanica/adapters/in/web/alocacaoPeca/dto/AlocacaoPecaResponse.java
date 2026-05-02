package br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.dto;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;

import java.util.UUID;

public record AlocacaoPecaResponse(
        UUID id,
        StatusRecursoEnum status,
        Integer quantidadeNecessaria,
        UUID prestacaoServicoId,
        UUID pecaId
) {}
