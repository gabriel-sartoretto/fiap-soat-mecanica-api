package br.com.fiap.soat.mecanica.application.usuario.usecase;

import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.usuario.UsuarioRepository;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.domain.valueobject.Senha;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarUsuarioUseCase {

    private final UsuarioRepository repository;
    private final PasswordEncoderPort passwordEncoderPort;

    public Usuario executar(String senha, String nome, String email, CargoEnum cargoEnum) {

        Email emailFormatado = new Email(email);
        repository.buscarPorEmail(emailFormatado.getValue())
                .ifPresent(usuario -> {
                    throw new RegraNegocioException("Usuário já cadastrada");
                });

        Senha senhaValidada = new Senha(senha);
        String senhaHash = passwordEncoderPort.encode(senhaValidada.getValor());
        Usuario usuario = new Usuario(nome, senhaHash, new Email(email), cargoEnum);
        return repository.salvar(usuario);
    }
}
