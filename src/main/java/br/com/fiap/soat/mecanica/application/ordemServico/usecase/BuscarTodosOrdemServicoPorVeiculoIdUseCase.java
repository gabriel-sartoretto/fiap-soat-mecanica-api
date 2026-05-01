package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarTodosOrdemServicoPorVeiculoIdUseCase {

    private final OrdemServicoRepository ordemServicoRepository;

    public List<OrdemServico> executar(UUID veiculoId) {

        return ordemServicoRepository.buscarTodosPorVeiculoId(veiculoId);
    }
}
