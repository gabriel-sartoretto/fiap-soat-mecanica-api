package br.com.fiap.soat.mecanica.adapters.out.persistence.peca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.peca.mapper.PecaMapper;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import br.com.fiap.soat.mecanica.domain.peca.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PecaRepositoryImpl implements PecaRepository {

    private final PecaJpaRepository pecaJpaRepository;
    private final PecaMapper pecaMapper;

    @Override
    public Peca salvar(Peca peca) {
        PecaEntity pecaEntity = pecaMapper.toEntity(peca);
        pecaJpaRepository.save(pecaEntity);
        return pecaMapper.toDomain(pecaEntity);
    }

    @Override
    public Optional<Peca> buscarPorId(UUID id) {
        return pecaJpaRepository.findById(id)
                .map(pecaMapper::toDomain);
    }

    @Override
    public Optional<Peca> buscarPorNome(String nome) {
        return pecaJpaRepository.findByNome(nome)
                .map(pecaMapper::toDomain);
    }
}
