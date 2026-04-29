package br.com.fiap.soat.mecanica.application.usuario.usecase;

import br.com.fiap.soat.mecanica.adapters.in.web.exception.SenhaInvalidaException;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticarUsuarioUseCase {

    private final PasswordEncoderPort encoder;
    private final JwtService jwtService;
    private final BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;

    public String login(String email, String senha) {

        Usuario usuario = buscarUsuarioPorEmailUseCase.executar(email);

        if (!encoder.matches(senha, usuario.getSenha())) {
            throw new SenhaInvalidaException("Senha inválida");
        }

        return jwtService.gerarToken(usuario.getEmail().getValue());
    }
}
