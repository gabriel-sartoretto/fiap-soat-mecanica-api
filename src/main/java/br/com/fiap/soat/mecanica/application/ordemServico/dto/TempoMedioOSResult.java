package br.com.fiap.soat.mecanica.application.ordemServico.dto;

import java.util.List;

public record TempoMedioOSResult(
        List<TempoMedioServicoResult> itens,
        String tempoTotal
) {}