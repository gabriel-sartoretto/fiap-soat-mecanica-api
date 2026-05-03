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
public class CadastrarAlocacaoPecaUseCase {

    private final AlocacaoPecaRepository repository;
    private final PecaRepository pecaRepository;
    private final PrestacaoServicoRepository prestacaoServicoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    @Transactional
    public AlocacaoPeca executar(Integer quantidade, UUID prestacaoId, UUID pecaId) {

        if (repository.existsByPrestacaoServicoIdAndPecaId(prestacaoId, pecaId)) {
            throw new RegraNegocioException("Peça já alocada nessa prestação");
        }

        Peca peca = pecaRepository.buscarPorId(pecaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));

        PrestacaoServico prestacao = prestacaoServicoRepository.buscarPorId(prestacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Prestação de serviço não encontrada"));
        prestacao.validarPodeAlterar();

        OrdemServico ordemServico = ordemServicoRepository.buscarPorId(prestacao.getOrdemServicoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));

        ordemServico.validarPermiteAlterarDiagnostico();

        AlocacaoPeca alocacao = new AlocacaoPeca(quantidade, prestacaoId, pecaId);
        AlocacaoPeca alocacaoSalva = repository.salvar(alocacao);

        peca.baixarEstoque(quantidade);
        pecaRepository.salvar(peca);

        BigDecimal valorPeca = prestacao.adicionarValorPeca(peca.getValorUnitario(), quantidade);
        prestacaoServicoRepository.salvar(prestacao);

        ordemServico.adicionarValor(valorPeca);
        ordemServicoRepository.salvar(ordemServico);

        return alocacaoSalva;
    }
}