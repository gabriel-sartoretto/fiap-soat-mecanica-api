package br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca.mapper.AlocacaoPecaMapper;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPecaRepository;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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
        return alocacaoPecaJpaRepository.existsByPrestacaoServicoIdAndPecaIdAndStatus(prestacaoId, pecaId, StatusRecursoEnum.ATIVO);
    }

    @Override
    public List<AlocacaoPeca> buscarTodosPorPrestacaoServicoId(UUID prestacaoId) {
        return alocacaoPecaJpaRepository.findAllByPrestacaoServicoId(prestacaoId)
                .stream()
                .map(alocacaoPecaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<AlocacaoPeca> buscarPorId(UUID id) {
        return alocacaoPecaJpaRepository.findById(id)
                .map(alocacaoPecaMapper::toDomain);
    }
}
