package br.com.fiap.soat.mecanica.domain.alocacaoPecas;

import java.util.UUID;

public interface AlocacaoPecaRepository {

    AlocacaoPeca salvar(AlocacaoPeca alocacao);

    boolean existsByPrestacaoServicoIdAndPecaId(UUID prestacaoId, UUID pecaId);
}