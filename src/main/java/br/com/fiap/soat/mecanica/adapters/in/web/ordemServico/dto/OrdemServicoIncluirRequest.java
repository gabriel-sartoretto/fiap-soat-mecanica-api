package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrdemServicoIncluirRequest(

        String observacao,

        @NotNull(message = "O veículo é obrigatório")
        UUID veiculoId
) {
}
