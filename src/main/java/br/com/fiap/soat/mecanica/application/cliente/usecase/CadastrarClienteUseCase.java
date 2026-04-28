package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto.ClienteIncluirRequest;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarClienteUseCase {

    private final ClienteRepository repository;

    public Cliente executar(ClienteIncluirRequest request) {
        Cliente cliente = new Cliente(request.get)
    }
}
