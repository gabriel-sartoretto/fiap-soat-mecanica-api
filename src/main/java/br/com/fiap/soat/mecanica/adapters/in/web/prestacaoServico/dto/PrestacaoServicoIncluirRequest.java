package br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record PrestacaoServicoIncluirRequest(

        @NotNull(message = "A quantidade necessária é obrigatório ")
        Integer quantidadeNecessaria,

        @NotNull(message = "O preço da mão de obra é obrigatório")
        BigDecimal precoMaoDeObra,

        @NotNull(message = "A Ordem de Serviço é obrigatória")
        UUID ordemServicoId,

        @NotNull(message = "O Serviço é obrigatório")
        UUID servicoId
) {}