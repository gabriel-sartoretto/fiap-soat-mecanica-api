package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarOrdemServicoPorIdUseCase {

    private final OrdemServicoRepository ordemServicoRepository;

    public OrdemServico executar(UUID id) {

        return ordemServicoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
    }
}
