package br.com.fiap.soat.mecanica.application.alocacaoPeca;

import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPecaRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CadastrarAlocacaoPecaUseCase {

    private final AlocacaoPecaRepository repository;
    private final PecaRepository pecaRepository;

    public AlocacaoPeca executar(Integer quantidade, UUID prestacaoId, UUID pecaId) {

        if (repository.existsByPrestacaoServicoIdAndPecaId(prestacaoId, pecaId)) {
            throw new RegraNegocioException("Peça já alocada nessa prestação");
        }

        Peca peca = pecaRepository.buscarPorId(pecaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Peça não encontrada"));

        if (peca.getQuantidadeEstoque() < quantidade) {
            throw new RegraNegocioException(String.format("Sem estoque para a peça %s, o estoque contém apenas %s", peca.getNome(), peca.getQuantidadeEstoque()));
        }

        AlocacaoPeca alocacao = new AlocacaoPeca(quantidade, prestacaoId, pecaId);
        return repository.salvar(alocacao);
    }
}