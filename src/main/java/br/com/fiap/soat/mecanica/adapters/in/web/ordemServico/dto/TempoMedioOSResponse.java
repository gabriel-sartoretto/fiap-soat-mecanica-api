package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto;

import java.util.List;

public record TempoMedioOSResponse(
        List<TempoMedioServicoItemResponse> servicos,
        String tempoTotal
) {}