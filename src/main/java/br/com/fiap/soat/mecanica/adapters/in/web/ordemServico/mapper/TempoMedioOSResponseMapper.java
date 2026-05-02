package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.TempoMedioOSResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.TempoMedioServicoItemResponse;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioOSResult;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;

import java.util.List;

public class TempoMedioOSResponseMapper {

    public static TempoMedioOSResponse toResponse(TempoMedioOSResult result) {
        if (result == null) {
            return null;
        }

        List<TempoMedioServicoItemResponse> itens = result.itens()
                .stream()
                .map(TempoMedioOSResponseMapper::toResponse)
                .toList();

        return new TempoMedioOSResponse(
                itens,
                result.tempoTotal()
        );
    }

    private static TempoMedioServicoItemResponse toResponse(TempoMedioServicoResult item) {
        return new TempoMedioServicoItemResponse(
                item.nomeServico(),
                item.tempoMedioSegundos()
        );
    }
}
