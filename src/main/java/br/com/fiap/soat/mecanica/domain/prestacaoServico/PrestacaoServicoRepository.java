package br.com.fiap.soat.mecanica.domain.prestacaoServico;

import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PrestacaoServicoRepository {

    PrestacaoServico salvar(PrestacaoServico ps);

    Optional<PrestacaoServico> buscarPorId(UUID id);

    List<PrestacaoServico> buscarTodosPorOrdemServicoId(UUID ordemServicoId);

    boolean existsByOrdemServicoIdAndServicoId(UUID ordemServicoId, UUID servicoId);

    List<TempoMedioServicoResult> calcularTempoMedioPorServicos(Set<UUID> servicoIds);
}
