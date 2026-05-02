package br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca.mapper.AlocacaoPecaMapper;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AlocacaoPecaRepositoryImpl implements AlocacaoPecaRepository {

    private final AlocacaoPecaJpaRepository alocacaoPecaJpaRepository;
    private final AlocacaoPecaMapper alocacaoPecaMapper;

    @Override
    public AlocacaoPeca salvar(AlocacaoPeca alocacao) {
        AlocacaoPecaEntity alocacaoPecaEntity = alocacaoPecaMapper.toEntity(alocacao);
        alocacaoPecaJpaRepository.save(alocacaoPecaEntity);
        return alocacaoPecaMapper.toDomain(alocacaoPecaEntity);
    }

    @Override
    public boolean existsByPrestacaoServicoIdAndPecaId(UUID prestacaoId, UUID pecaId) {
        return alocacaoPecaJpaRepository.existsByPrestacaoServicoIdAndPecaId(prestacaoId, pecaId);
    }
}
