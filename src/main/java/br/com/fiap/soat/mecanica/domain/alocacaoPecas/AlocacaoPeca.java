package br.com.fiap.soat.mecanica.domain.alocacaoPecas;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlocacaoPeca extends Principal {

    private UUID id;
    private Integer quantidadeNecessaria;
    private UUID prestacaoServicoId;
    private UUID pecaId;

    public AlocacaoPeca(Integer quantidadeNecessaria, UUID prestacaoServicoId, UUID pecaId) {
        validar(quantidadeNecessaria, prestacaoServicoId, pecaId);
        this.quantidadeNecessaria = quantidadeNecessaria;
        this.prestacaoServicoId = prestacaoServicoId;
        this.pecaId = pecaId;
    }

    public static AlocacaoPeca reconstruir(UUID id,
                                           StatusRecursoEnum status,
                                           Integer quantidadeNecessaria,
                                           UUID prestacaoServicoId,
                                           UUID pecaId) {
        AlocacaoPeca alocacaoPeca = new AlocacaoPeca();
        alocacaoPeca.id = id;
        alocacaoPeca.status = status;
        alocacaoPeca.quantidadeNecessaria = quantidadeNecessaria;
        alocacaoPeca.prestacaoServicoId = prestacaoServicoId;
        alocacaoPeca.pecaId = pecaId;
        return alocacaoPeca;
    }

    private void validar(Integer quantidade, UUID prestacaoId, UUID pecaId) {
        if (quantidade == null || quantidade <= 0) {
            throw new RegraNegocioException("Quantidade obrigatória");
        }
        if (prestacaoId == null) {
            throw new RegraNegocioException("Prestação obrigatória");
        }
        if (pecaId == null) {
            throw new RegraNegocioException("Peça obrigatória");
        }
    }
}