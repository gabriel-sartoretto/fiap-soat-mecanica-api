package br.com.fiap.soat.mecanica.application.usuario;

import br.com.fiap.soat.mecanica.adapters.in.web.usuario.dto.UsuarioIncluirRequest;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastrarUsuarioUseCase {

    private final UsuarioRepository repository;
    private final PasswordEncoderPort passwordEncoderPort;

    public Usuario executar(UsuarioIncluirRequest request) {
        String senhaHash = passwordEncoderPort.encode(request.senha());
        Usuario usuario = new Usuario(request.nome(), request.email(),
                senhaHash, request.cargoEnum());
        return repository.salvar(usuario);
    }
}
