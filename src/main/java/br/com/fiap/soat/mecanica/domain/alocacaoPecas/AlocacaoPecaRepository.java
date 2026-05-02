package br.com.fiap.soat.mecanica.domain.alocacaoPecas;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlocacaoPecaRepository {

    AlocacaoPeca salvar(AlocacaoPeca alocacao);

    boolean existsByPrestacaoServicoIdAndPecaId(UUID prestacaoId, UUID pecaId);

    List<AlocacaoPeca> buscarTodosPorPrestacaoServicoId(UUID prestacaoId);

    Optional<AlocacaoPeca> buscarPorId(UUID id);
}