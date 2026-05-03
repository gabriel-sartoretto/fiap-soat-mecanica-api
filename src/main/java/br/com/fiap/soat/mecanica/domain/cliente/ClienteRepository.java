package br.com.fiap.soat.mecanica.domain.cliente;

import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(UUID id);

    Optional<Cliente> buscarPorCpf(CPF documento);

    Optional<Cliente> buscarPorCnpj(CNPJ documento);

    List<Cliente> buscarPorUsuarioId(UUID usuarioId);
}
