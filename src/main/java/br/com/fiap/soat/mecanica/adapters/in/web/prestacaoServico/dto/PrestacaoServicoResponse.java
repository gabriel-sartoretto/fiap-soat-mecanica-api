package br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.dto;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PrestacaoServicoResponse(
        UUID id,
        StatusRecursoEnum status,
        Integer quantidadeEstoque,
        BigDecimal precoMaoDeObra,
        BigDecimal subtotal,
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        UUID ordemServicoId,
        UUID servicoId
) {}
