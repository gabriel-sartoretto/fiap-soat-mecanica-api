package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.adapters.in.web.security.CurrentUserProvider;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.domain.valueobject.Telefone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarClienteUseCase {

    private final ClienteRepository repository;
    private final CurrentUserProvider currentUser;

    public Cliente executar(String nome, String cpf, String cnpj, String email, String telefone) {

        Usuario usuario = currentUser.get();

        Cliente cliente = new Cliente(
                nome,
                cpf == null ? null : new CPF(cpf),
                cnpj == null ? null : new CNPJ(cnpj),
                new Email(email),
                telefone == null ? null : new Telefone(telefone),
                usuario.getId()
        );
        return repository.salvar(cliente);
    }
}
