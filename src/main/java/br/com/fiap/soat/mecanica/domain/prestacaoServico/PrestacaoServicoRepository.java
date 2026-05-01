package br.com.fiap.soat.mecanica.domain.prestacaoServico;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrestacaoServicoRepository {

    PrestacaoServico salvar(PrestacaoServico ps);

    Optional<PrestacaoServico> buscarPorId(UUID id);

    List<PrestacaoServico> buscarTodosPorOrdemServicoId(UUID ordemServicoId);
}
