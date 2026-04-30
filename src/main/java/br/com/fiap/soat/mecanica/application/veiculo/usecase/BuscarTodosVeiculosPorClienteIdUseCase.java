package br.com.fiap.soat.mecanica.application.veiculo.usecase;

import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarTodosVeiculosPorClienteIdUseCase {

    private final VeiculoRepository veiculoRepository;

    public List<Veiculo> executar(UUID clienteId) {
        return veiculoRepository.buscarTodosPorClienteId(clienteId);
    }
}
