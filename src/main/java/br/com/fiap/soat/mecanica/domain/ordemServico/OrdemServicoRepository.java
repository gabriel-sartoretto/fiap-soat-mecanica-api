package br.com.fiap.soat.mecanica.domain.ordemServico;

import java.util.Optional;
import java.util.UUID;

public interface OrdemServicoRepository {

    OrdemServico salvar(OrdemServico ordemServico);

    Optional<OrdemServico> buscarPorId(UUID id);
}
