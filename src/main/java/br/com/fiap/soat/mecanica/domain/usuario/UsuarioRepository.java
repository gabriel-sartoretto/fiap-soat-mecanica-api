package br.com.fiap.soat.mecanica.domain.usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorEmail(String email);

    Optional<Usuario> buscarPorId(UUID id);
}
