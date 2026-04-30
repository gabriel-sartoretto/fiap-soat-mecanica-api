package br.com.fiap.soat.mecanica.domain.veiculo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeiculoRepository {

    Veiculo salvar(Veiculo veiculo);

    Optional<Veiculo> buscarPorId(UUID id);

    Optional<Veiculo> buscarPorPlaca(String placa);

    List<Veiculo> buscarTodosPorClienteId(UUID clienteId);
}
