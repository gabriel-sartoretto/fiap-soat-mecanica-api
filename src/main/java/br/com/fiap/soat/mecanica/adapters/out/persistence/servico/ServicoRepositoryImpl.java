package br.com.fiap.soat.mecanica.adapters.out.persistence.servico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.servico.mapper.ServicoMapper;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import br.com.fiap.soat.mecanica.domain.servico.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ServicoRepositoryImpl implements ServicoRepository {

    private final ServicoJpaRepository jpaRepository;
    private final ServicoMapper mapper;

    @Override
    public Servico salvar(Servico servico) {
        ServicoEntity entity = mapper.toEntity(servico);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Servico> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Servico> buscarPorNome(String nome) {
        return jpaRepository.findByNome(nome);
    }
}
