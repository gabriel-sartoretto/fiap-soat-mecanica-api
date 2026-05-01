package br.com.fiap.soat.mecanica.application.usuario.usecase;

import br.com.fiap.soat.mecanica.domain.exception.RecursoNaoEncontradoException;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.usuario.UsuarioRepository;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuscarUsuarioPorEmailUseCase {

    private final UsuarioRepository repository;

    public Usuario executar(String email) {
        Email emailFormatado = new Email(email);
        return repository.buscarPorEmail(emailFormatado.getValue())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }
}
