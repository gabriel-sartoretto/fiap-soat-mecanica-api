package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioOSResult;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
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

        List<TempoMedioServicoResult> itens =
                prestacaoRepository.calcularTempoMedioPorServicos(servicoIds);

        double totalSegundos = itens.stream()
                .map(TempoMedioServicoResult::tempoMedioSegundos)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();

        return new TempoMedioOSResult(
                itens,
                totalSegundos
        );
    }
}