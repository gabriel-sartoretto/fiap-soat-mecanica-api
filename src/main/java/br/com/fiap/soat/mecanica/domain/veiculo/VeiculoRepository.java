package br.com.fiap.soat.mecanica.domain.veiculo;

import java.util.Optional;
import java.util.UUID;

public interface VeiculoRepository {

    Veiculo salvar(Veiculo veiculo);

    Optional<Veiculo> buscarPorId(UUID id);
}
