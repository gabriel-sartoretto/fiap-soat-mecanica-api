package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.domain.valueobject.Telefone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CadastrarClienteUseCase {

    private final ClienteRepository repository;

<<<<<<< HEAD
    public Cliente executar(String nome, String cpf, String cnpj, String email, String telefone, UUID usuarioId) {
        Cliente cliente = new Cliente(
                nome,
                cpf == null ? null : new CPF(cpf),
                cnpj == null ? null : new CNPJ(cnpj),
                new Email(email),
                telefone == null ? null : new Telefone(telefone),
                usuarioId
        );
        return repository.salvar(cliente);
=======
    public Cliente executar(ClienteIncluirRequest request) {
        throw new UnsupportedOperationException("Cadastro de cliente ainda não implementado.");
>>>>>>> 1b337a9 (feat: implementação módulo de peças com validações e segurança)
    }
}
