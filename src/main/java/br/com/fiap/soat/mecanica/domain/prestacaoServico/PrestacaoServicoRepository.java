package br.com.fiap.soat.mecanica.domain.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.projection.TempoMedioServicoProjection;

import java.util.*;

public interface PrestacaoServicoRepository {

    PrestacaoServico salvar(PrestacaoServico ps);

    Optional<PrestacaoServico> buscarPorId(UUID id);

    List<PrestacaoServico> buscarTodosPorOrdemServicoId(UUID ordemServicoId);

    boolean existsByOrdemServicoIdAndServicoId(UUID ordemServicoId, UUID servicoId);

    List<TempoMedioServicoProjection> calcularTempoMedioPorServicos(Set<UUID> servicoIds);
}
