package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuscarClientePorUsuarioIdUseCase {

    private final ClienteRepository clienteRepository;

    public List<Cliente> executar(UUID usuarioId) {
        return clienteRepository.buscarPorUsuarioId(usuarioId);
    }
}
