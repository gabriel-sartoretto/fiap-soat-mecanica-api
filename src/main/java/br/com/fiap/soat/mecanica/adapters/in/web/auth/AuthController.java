package br.com.fiap.soat.mecanica.adapters.in.web.auth;

import br.com.fiap.soat.mecanica.adapters.in.web.auth.dto.LoginRequest;
import br.com.fiap.soat.mecanica.application.usuario.usecase.AutenticarUsuarioUseCase;
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

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = autenticarUsuarioUseCase.login(request.email(), request.senha());

        return ResponseEntity.ok(Map.of("token", token));
    }
}