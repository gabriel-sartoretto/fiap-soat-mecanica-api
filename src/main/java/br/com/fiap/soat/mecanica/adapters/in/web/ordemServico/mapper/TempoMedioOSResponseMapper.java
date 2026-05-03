package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.TempoMedioOSResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.TempoMedioServicoItemResponse;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioOSResult;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;

import java.time.Duration;
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
                formatarTempo(result.tempoTotalSegundos())
        );
    }

    private static TempoMedioServicoItemResponse toResponse(TempoMedioServicoResult item) {
        return new TempoMedioServicoItemResponse(
                item.nomeServico(),
                formatarTempo(item.tempoMedioSegundos())
        );
    }

    private static String formatarTempo(Double segundos) {
        if (segundos == null) {
            return "Sem histórico";
        }

        Duration duration = Duration.ofSeconds(segundos.longValue());

        long horas = duration.toHours();
        long minutos = duration.toMinutesPart();

        if (horas > 0) {
            return horas + "h " + minutos + "min";
        }

        return minutos + " min";
    }
}