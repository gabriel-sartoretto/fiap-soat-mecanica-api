package br.com.fiap.soat.mecanica.application.usuario.usecase;

import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.usuario.UsuarioRepository;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarUsuarioUseCase {

    private final UsuarioRepository repository;
    private final PasswordEncoderPort passwordEncoderPort;

    public Usuario executar(String senha, String nome, String email, CargoEnum cargoEnum) {
        repository.buscarPorEmail(email)
                .ifPresent(usuario -> {
                    throw new RegraNegocioException("Placa já cadastrada");
                });
        String senhaHash = passwordEncoderPort.encode(senha);
        Usuario usuario = new Usuario(nome, senhaHash, new Email(email), cargoEnum);
        return repository.salvar(usuario);
    }
}
