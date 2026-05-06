package br.com.fiap.soat.mecanica.application.cliente.usecase;

import br.com.fiap.soat.mecanica.application.security.CurrentUserPort;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.cliente.ClienteRepository;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
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

    private final ClienteRepository clienteRepository;
    private final CurrentUserPort currentUser;

    public Cliente executar(String nome, String cpf, String cnpj, String email, String telefone) {

        CPF cpfFormatado = cpf == null ? null : new CPF(cpf);
        CNPJ cnpjFormatado = cnpj == null ? null : new CNPJ(cnpj);

        if (cpfFormatado != null) {
            clienteRepository.buscarPorCpf(cpfFormatado)
                    .ifPresent(cliente -> {
                        throw new RegraNegocioException("CPF já existente");
                    });
        }

        if (cnpjFormatado != null) {
            clienteRepository.buscarPorCnpj(cnpjFormatado)
                    .ifPresent(cliente -> {
                        throw new RegraNegocioException("CNPJ já existente");
                    });
        }

        Usuario usuario = currentUser.get();

        Cliente cliente = new Cliente(
                nome,
                cpfFormatado,
                cnpjFormatado,
                new Email(email),
                telefone == null ? null : new Telefone(telefone),
                usuario.getId()
        );
        return clienteRepository.salvar(cliente);
    }
}
