package br.com.fiap.soat.mecanica.adapters.out.persistence.cliente;

import br.com.fiap.soat.mecanica.adapters.out.persistence.cliente.mapper.ClienteMapper;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
    public Optional<Cliente> buscarPorDocumento(String documento) {
        //TODO CRIAR ESSE FLUXO
        return Optional.empty();
    }
}
