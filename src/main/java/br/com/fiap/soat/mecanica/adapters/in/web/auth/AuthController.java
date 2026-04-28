package br.com.fiap.soat.mecanica.adapters.in.web.auth;

import br.com.fiap.soat.mecanica.adapters.in.web.auth.dto.LoginRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.exception.SenhaInvalidaException;
import br.com.fiap.soat.mecanica.adapters.out.security.JwtService;
import br.com.fiap.soat.mecanica.application.usuario.usecase.BuscarUsuarioPorEmailUseCase;
import br.com.fiap.soat.mecanica.application.usuario.usecase.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final BuscarUsuarioPorEmailUseCase buscarUsuarioPorEmailUseCase;
    private final PasswordEncoderPort encoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        var usuario = buscarUsuarioPorEmailUseCase.executar(request.email());

        if (!encoder.matches(request.senha(), usuario.getSenha())) {
            throw new SenhaInvalidaException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario.getEmail().getValue());

        return ResponseEntity.ok(Map.of("token", token));
    }
}