package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.valueobject.Telefone;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.TE;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlterarClienteUseCase {

    private final ClienteRepository repository;

    public Cliente executar(UUID clienteId, String nome, String telefone) {

        Cliente cliente = repository.buscarPorId(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));

        cliente.alterar(nome, telefone == null ? null : new Telefone(telefone));
        return repository.salvar(cliente);
    }
}
