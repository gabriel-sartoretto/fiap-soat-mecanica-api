package br.com.fiap.soat.mecanica.infrastructure.persistence.veiculo;

import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import br.com.fiap.soat.mecanica.infrastructure.persistence.veiculo.mapper.VeiculoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VeiculoRepositoryImpl implements VeiculoRepository {

    private final VeiculoJpaRepository jpaRepository;

    @Override
    public void salvar(Veiculo veiculo) {
        VeiculoEntity veiculoEntity = VeiculoMapper.toEntity(veiculo);
        jpaRepository.save(veiculoEntity);
    }
}
