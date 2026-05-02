package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PagarEEntregarOrdemServicoUseCase {

    private final OrdemServicoRepository ordemServicoRepository;

    public OrdemServico executar(UUID id) {

        OrdemServico ordemServico = ordemServicoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de Serviço não encontrada"));
        ordemServico.entregar();
        return ordemServicoRepository.salvar(ordemServico);
    }
}
