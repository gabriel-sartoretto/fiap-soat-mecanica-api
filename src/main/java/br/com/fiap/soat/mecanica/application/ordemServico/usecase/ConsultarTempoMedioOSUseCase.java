package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.projection.TempoMedioServicoProjection;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioOSResult;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarTempoMedioOSUseCase {

    private final PrestacaoServicoRepository prestacaoRepository;

    public TempoMedioOSResult executar(UUID osId) {

        Set<UUID> servicoIds = prestacaoRepository
                .buscarTodosPorOrdemServicoId(osId)
                .stream()
                .map(PrestacaoServico::getServicoId)
                .collect(Collectors.toSet());

        List<TempoMedioServicoProjection> projections =
                prestacaoRepository.calcularTempoMedioPorServicos(servicoIds);

        List<TempoMedioServicoResult> itens = new ArrayList<>();

        long totalSegundos = 0;

        for (TempoMedioServicoProjection p : projections) {

            Double segundos = p.getTempoMedioSegundos();

            if (segundos != null) {
                totalSegundos += segundos.longValue();
            }

            itens.add(new TempoMedioServicoResult(
                    p.getNomeServico(),
                    formatarTempo(segundos)
            ));
        }

        return new TempoMedioOSResult(
                itens,
                formatarTempo((double) totalSegundos)
        );
    }

    private String formatarTempo(Double segundos) {
        if (segundos == null) return "Sem histórico";

        Duration d = Duration.ofSeconds(segundos.longValue());

        long horas = d.toHours();
        long minutos = d.toMinutesPart();

        if (horas > 0) {
            return horas + "h " + minutos + "min";
        }
        return minutos + " min";
    }
}