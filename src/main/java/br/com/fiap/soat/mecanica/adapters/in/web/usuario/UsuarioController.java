package br.com.fiap.soat.mecanica.adapters.in.web.usuario;

import br.com.fiap.soat.mecanica.adapters.in.web.usuario.dto.UsuarioIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.usuario.dto.UsuarioResponse;
import br.com.fiap.soat.mecanica.adapters.in.web.usuario.mapper.UsuarioResponseMapper;
import br.com.fiap.soat.mecanica.application.usuario.AutenticarUsuarioUseCase;
import br.com.fiap.soat.mecanica.application.usuario.CadastrarUsuarioUseCase;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @PostMapping()
    @Operation(summary = "Cadastrar um usuário")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioIncluirRequest request) {
        Usuario usuario = cadastrarUsuarioUseCase.executar(request);
        return ResponseEntity.ok(UsuarioResponseMapper.toResponse(usuario));
    }
}
