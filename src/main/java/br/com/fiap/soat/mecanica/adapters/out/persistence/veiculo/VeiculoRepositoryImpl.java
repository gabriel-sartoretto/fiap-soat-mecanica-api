package br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo;

import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.domain.veiculo.VeiculoRepository;
import br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo.mapper.VeiculoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class VeiculoRepositoryImpl implements VeiculoRepository {

    private final VeiculoJpaRepository veiculoJpaRepository;
    private final VeiculoMapper veiculoMapper;

    @Override
    public Veiculo salvar(Veiculo veiculo) {
        VeiculoEntity veiculoEntity = veiculoMapper.toEntity(veiculo);
        veiculoJpaRepository.save(veiculoEntity);
        return veiculoMapper.toDomain(veiculoEntity);
    }

    @Override
    public Optional<Veiculo> buscarPorId(UUID id) {
        return veiculoJpaRepository.findById(id)
                .map(veiculoMapper::toDomain);
    }

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return veiculoJpaRepository.findByPlaca(placa)
                .map(veiculoMapper::toDomain);
    }

    @Override
    public List<Veiculo> buscarTodosPorClienteId(UUID clienteId) {
        return veiculoJpaRepository.findAllByClienteId(clienteId)
                .stream()
                .map(veiculoMapper::toDomain)
                .toList();
    }
}
