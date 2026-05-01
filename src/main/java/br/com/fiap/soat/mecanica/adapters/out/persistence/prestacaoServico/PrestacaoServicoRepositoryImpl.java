package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.mapper.PrestacaoServicoMapper;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
}
