package br.com.fiap.soat.mecanica.domain.peca;

import java.util.Optional;
import java.util.UUID;

public interface PecaRepository {

    Peca salvar(Peca peca);

    Optional<Peca> buscarPorId(UUID id);

    Optional<Peca> buscarPorNome(String nome);
}
