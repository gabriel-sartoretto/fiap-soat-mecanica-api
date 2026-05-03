package br.com.fiap.soat.mecanica.application.ordemServico.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class IniciarExecucaoOrdemServicoUseCase {

    private final OrdemServicoRepository ordemServicoRepository;
    private final PrestacaoServicoRepository prestacaoServicoRepository;

    @Transactional
    public OrdemServico executar(UUID ordemServicoId) {

        OrdemServico ordemServico = ordemServicoRepository.buscarPorId(ordemServicoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));

        List<PrestacaoServico> prestacoesAtivas = prestacaoServicoRepository
                .buscarTodosPorOrdemServicoId(ordemServicoId)
                .stream()
                .filter(PrestacaoServico::isAtivo)
                .toList();

        if (prestacoesAtivas.isEmpty()) {
            throw new RegraNegocioException("Ordem de serviço não possui prestações ativas");
        }

        ordemServico.iniciarExecucao();

        for (PrestacaoServico prestacao : prestacoesAtivas) {
            if (prestacao.getDataInicio() == null) {
                prestacao.iniciarServico(ordemServico.isEmExecucao());
                prestacaoServicoRepository.salvar(prestacao);
            }
        }

        return ordemServicoRepository.salvar(ordemServico);
    }
}