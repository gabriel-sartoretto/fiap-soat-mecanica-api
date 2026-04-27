package br.com.fiap.soat.mecanica.adapters.out.persistence.usuario;

import br.com.fiap.soat.mecanica.adapters.out.persistence.usuario.mapper.UsuarioMapper;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final UsuarioJpaRepository repository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public Usuario salvar(Usuario usuario) {
        UsuarioEntity usuarioEntity = usuarioMapper.toEntity(usuario);
        repository.save(usuarioEntity);
        return usuarioMapper.toDomain(usuarioEntity);
    }

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .map(usuarioMapper::toDomain);
    }
}
