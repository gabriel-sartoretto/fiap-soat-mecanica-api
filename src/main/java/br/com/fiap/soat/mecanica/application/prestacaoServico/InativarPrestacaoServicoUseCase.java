package br.com.fiap.soat.mecanica.application.prestacaoServico;

import br.com.fiap.soat.mecanica.application.alocacaoPeca.InativarAlocacaoPecaUseCase;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPecaRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InativarPrestacaoServicoUseCase {

    private final PrestacaoServicoRepository prestacaoServicoRepository;
    private final AlocacaoPecaRepository alocacaoPecaRepository;
    private final InativarAlocacaoPecaUseCase inativarAlocacaoPecaUseCase;
    private final OrdemServicoRepository ordemServicoRepository;

    @Transactional
    public PrestacaoServico executar(UUID id) {

        PrestacaoServico ps = prestacaoServicoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prestação de serviço não encontrada"));
        ps.validarPodeAlterar();

        OrdemServico ordemServico = ordemServicoRepository.buscarPorId(ps.getOrdemServicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
        ordemServico.validarPermiteAlterarDiagnostico();

        List<AlocacaoPeca> alocacoes = alocacaoPecaRepository.buscarTodosPorPrestacaoServicoId(id);
        alocacoes.stream()
                .filter(AlocacaoPeca::isAtivo)
                .forEach(alocacao -> inativarAlocacaoPecaUseCase.executar(alocacao.getId()));

        // Para não sobrescrever o subTotal
        PrestacaoServico prestacaoAtualizada = prestacaoServicoRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prestação de serviço não encontrada"));

        ordemServico.removerValor(prestacaoAtualizada.getSubtotal());

        prestacaoAtualizada.inativar();

        ordemServicoRepository.salvar(ordemServico);
        return prestacaoServicoRepository.salvar(prestacaoAtualizada);
    }
}
