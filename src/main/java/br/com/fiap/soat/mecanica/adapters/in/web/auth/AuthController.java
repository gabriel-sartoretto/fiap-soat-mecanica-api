package br.com.fiap.soat.mecanica.adapters.in.web.auth;

import br.com.fiap.soat.mecanica.adapters.in.web.auth.dto.LoginRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.auth.dto.LoginResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.auth.mapper.AuthResponseMapper;
import br.com.fiap.soat.mecanica.application.usuario.usecase.AutenticarUsuarioUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @PostMapping("/login")
    @Operation(summary = "Login na API usando email e senha")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = autenticarUsuarioUseCase.login(request.email(), request.senha());

        return ResponseEntity.ok(AuthResponseMapper.toResponse(token));
    }
}