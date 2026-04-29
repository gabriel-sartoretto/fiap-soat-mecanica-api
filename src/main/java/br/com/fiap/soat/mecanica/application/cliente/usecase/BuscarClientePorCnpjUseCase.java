package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarClientePorCnpjUseCase {

    private final ClienteRepository repository;

    public Cliente executar(String cnpj) {

        CNPJ documento = new CNPJ(cnpj);

        return repository.buscarPorCnpj(documento)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Cliente não encontrado"));
    }
}