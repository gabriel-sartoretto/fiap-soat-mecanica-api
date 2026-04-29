package br.com.fiap.soat.mecanica.adapters.in.web.peca.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record PecaIncluirRequest(

        @NotBlank(message = "O nome nao pode ser nulo ou vazio")
        String nome,

        @NotBlank(message = "A marca nao pode ser nula ou vazia")
        String marca,

        @NotNull(message = "O valor unitario nao pode ser nulo")
        @PositiveOrZero(message = "O valor unitario nao pode ser negativo")
        BigDecimal valorUnitario,

        @NotNull(message = "A quantidade em estoque nao pode ser nula")
        @PositiveOrZero(message = "A quantidade em estoque nao pode ser negativa")
        Integer quantidadeEstoque
) {}
