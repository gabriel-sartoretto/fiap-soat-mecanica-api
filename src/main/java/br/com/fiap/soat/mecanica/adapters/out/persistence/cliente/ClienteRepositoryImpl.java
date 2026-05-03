package br.com.fiap.soat.mecanica.adapters.out.persistence.cliente;

import br.com.fiap.soat.mecanica.adapters.out.persistence.cliente.mapper.ClienteMapper;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteJpaRepository clienteJpaRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public Cliente salvar(Cliente cliente) {
        ClienteEntity clienteEntity = clienteMapper.toEntity(cliente);
        clienteJpaRepository.save(clienteEntity);
        return clienteMapper.toDomain(clienteEntity);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return clienteJpaRepository.findById(id)
                .map(clienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(CPF cpf) {
        return clienteJpaRepository.findByCpf(cpf.getValue())
                .map(clienteMapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCnpj(CNPJ cnpj) {
        return clienteJpaRepository.findByCnpj(cnpj.getValue())
                .map(clienteMapper::toDomain);
    }

    @Override
    public List<Cliente> buscarPorUsuarioId(UUID usuarioId) {
        return clienteJpaRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(clienteMapper::toDomain)
                .toList();
    }
}
