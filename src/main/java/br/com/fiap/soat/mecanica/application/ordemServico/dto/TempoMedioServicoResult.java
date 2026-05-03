package br.com.fiap.soat.mecanica.application.ordemServico.dto;

public record TempoMedioServicoResult(
        String nomeServico,
        Double tempoMedioSegundos
) {}