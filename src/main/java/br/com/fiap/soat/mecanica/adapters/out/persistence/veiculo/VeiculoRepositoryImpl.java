package br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo;

import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo.mapper.VeiculoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VeiculoRepositoryImpl implements VeiculoRepository {

    private final VeiculoJpaRepository jpaRepository;
    private final VeiculoMapper mapper;

    @Override
    public Veiculo salvar(Veiculo veiculo) {
        VeiculoEntity veiculoEntity = mapper.toEntity(veiculo);
        jpaRepository.save(veiculoEntity);
        return mapper.toDomain(veiculoEntity);
    }

    @Override
    public Optional<Veiculo> buscarPorId(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
}
