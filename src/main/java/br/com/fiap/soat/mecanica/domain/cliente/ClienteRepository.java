package br.com.fiap.soat.mecanica.domain.cliente;

import java.util.Optional;

public interface ClienteRepository {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorDocumento(String documento);
}
