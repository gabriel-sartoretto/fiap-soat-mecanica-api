package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.mapper.PrestacaoServicoMapper;
import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.projection.TempoMedioServicoProjection;
import br.com.fiap.soat.mecanica.application.ordemServico.dto.TempoMedioServicoResult;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class PrestacaoServicoRepositoryImpl implements PrestacaoServicoRepository {

    private final PrestacaoServicoJpaRepository prestacaoServicoJpaRepository;
    private final PrestacaoServicoMapper prestacaoServicoMapper;

    @Override
    public PrestacaoServico salvar(PrestacaoServico ps) {
        PrestacaoServicoEntity prestacaoServicoEntity = prestacaoServicoMapper.toEntity(ps);
        prestacaoServicoJpaRepository.save(prestacaoServicoEntity);
        return prestacaoServicoMapper.toDomain(prestacaoServicoEntity);
    }

    @Override
    public Optional<PrestacaoServico> buscarPorId(UUID id) {
        return prestacaoServicoJpaRepository.findById(id)
                .map(prestacaoServicoMapper::toDomain);
    }

    @Override
    public List<PrestacaoServico> buscarTodosPorOrdemServicoId(UUID ordemServicoId) {
        return prestacaoServicoJpaRepository.findAllByOrdemServicoId(ordemServicoId)
                .stream()
                .map(prestacaoServicoMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByOrdemServicoIdAndServicoId(UUID ordemServicoId, UUID servicoId) {
        return prestacaoServicoJpaRepository.existsByOrdemServicoIdAndServicoId(ordemServicoId, servicoId);
    }

    @Override
    public List<TempoMedioServicoResult> calcularTempoMedioPorServicos(Set<UUID> servicoIds) {
        return prestacaoServicoJpaRepository.calcularTempoMedioPorServicos(servicoIds)
                .stream()
                .map(projection -> new TempoMedioServicoResult(
                        projection.getNomeServico(),
                        projection.getTempoMedioSegundos()
                ))
                .toList();
    }
}
