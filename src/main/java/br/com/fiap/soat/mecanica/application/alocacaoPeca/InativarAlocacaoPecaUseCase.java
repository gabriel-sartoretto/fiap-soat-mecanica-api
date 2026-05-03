package br.com.fiap.soat.mecanica.application.alocacaoPeca;

import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPecaRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServicoRepository;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InativarAlocacaoPecaUseCase {

    private final AlocacaoPecaRepository alocacaoPecaRepository;
    private final PecaRepository pecaRepository;
    private final PrestacaoServicoRepository prestacaoServicoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    @Transactional
    public AlocacaoPeca executar(UUID alocacaoId) {

        AlocacaoPeca alocacao = alocacaoPecaRepository.buscarPorId(alocacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Alocação de peça não encontrada"));

        if (alocacao.isInativo()) {
            throw new RegraNegocioException("Alocação de peça já está inativa");
        }

        Peca peca = pecaRepository.buscarPorId(alocacao.getPecaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));

        PrestacaoServico prestacaoServico = prestacaoServicoRepository.buscarPorId(alocacao.getPrestacaoServicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prestação de serviço não encontrada"));
        prestacaoServico.validarPodeAlterar();

        alocacao.inativar();

        BigDecimal valorPeca = prestacaoServico.removerValorPeca(peca.getValorUnitario(),
                alocacao.getQuantidadeNecessaria());

        peca.reporEstoque(alocacao.getQuantidadeNecessaria());

        if (prestacaoServico.isAtivo()) {
            OrdemServico ordemServico = ordemServicoRepository.buscarPorId(prestacaoServico.getOrdemServicoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));

            ordemServico.validarPermiteAlterarDiagnostico();
            ordemServico.removerValor(valorPeca);
            ordemServicoRepository.salvar(ordemServico);
        }

        alocacaoPecaRepository.salvar(alocacao);
        pecaRepository.salvar(peca);
        prestacaoServicoRepository.salvar(prestacaoServico);
        return alocacao;
    }
}