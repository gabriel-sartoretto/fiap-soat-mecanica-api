package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AprovarOrcamentoUseCase {

    private final OrdemServicoRepository ordemServicoRepository;

    @Transactional
    public OrdemServico executar(UUID id) {
        OrdemServico os = ordemServicoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));

        os.iniciarExecucao();

        return ordemServicoRepository.salvar(os);
    }
}
