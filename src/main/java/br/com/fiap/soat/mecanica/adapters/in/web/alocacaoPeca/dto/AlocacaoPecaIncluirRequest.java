package br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record AlocacaoPecaIncluirRequest(

        @NotNull(message = "A quantidade necessária não pode ser nula")
        @PositiveOrZero(message = "A quantidade necessária nao pode ser negativo")
        Integer quantidadeNecessaria,

        @NotNull(message = "A prestação de serviço não pode ser nula")
        UUID prestacaoServicoId,

        @NotNull(message = "A peça não pode ser nula")
        UUID pecaId
) {}