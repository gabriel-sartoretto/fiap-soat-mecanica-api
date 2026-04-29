package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarClientePorCpfUseCase {

    private final ClienteRepository repository;

    public Cliente executar(String cpf) {

        CPF documento = new CPF(cpf);

        return repository.buscarPorCpf(documento)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente não encontrado"));
    }
}
