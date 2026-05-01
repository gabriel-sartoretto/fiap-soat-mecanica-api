package br.com.fiap.soat.mecanica.domain.servico;

import java.util.Optional;
import java.util.UUID;

public interface ServicoRepository {

    Servico salvar(Servico servico);

    Optional<Servico> buscarPorId(UUID id);

    Optional<Servico> buscarPorNome(String nome);
}
