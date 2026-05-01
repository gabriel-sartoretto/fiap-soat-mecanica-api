package br.com.fiap.soat.mecanica.adapters.in.web.peca.dto;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;

import java.math.BigDecimal;
import java.util.UUID;

public record PecaResponse(

        UUID id,
        StatusRecursoEnum status,
        String nome,
        String marca,
        BigDecimal valorUnitario,
        Integer quantidadeEstoque
) {}
