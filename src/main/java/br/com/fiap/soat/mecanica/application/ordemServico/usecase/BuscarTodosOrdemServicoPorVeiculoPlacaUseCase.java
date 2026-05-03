package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.domain.valueobject.Placa;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuscarTodosOrdemServicoPorVeiculoPlacaUseCase {

    private final VeiculoRepository veiculoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    public List<OrdemServico> executar(String placa) {

        Placa placaFormatada = new Placa(placa);
        Veiculo veiculo = veiculoRepository.buscarPorPlaca(placaFormatada.getValue())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));

        return ordemServicoRepository.buscarTodosPorVeiculoId(veiculo.getId());
    }
}
