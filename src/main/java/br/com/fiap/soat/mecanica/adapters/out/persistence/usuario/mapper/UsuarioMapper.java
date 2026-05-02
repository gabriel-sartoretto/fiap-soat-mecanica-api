package br.com.fiap.soat.mecanica.adapters.out.persistence.usuario.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.usuario.UsuarioEntity;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioEntity toEntity(Usuario usuario) {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(usuario.getId());
        usuarioEntity.setStatus(usuario.getStatus());
        usuarioEntity.setNome(usuario.getNome());
        usuarioEntity.setEmail(usuario.getEmail().getValue());
        usuarioEntity.setSenha(usuario.getSenha());
        usuarioEntity.setCargoEnum(usuario.getCargoEnum());
        return usuarioEntity;
    }

    public Usuario toDomain(UsuarioEntity usuarioEntity) {
        return Usuario.reconstruir(usuarioEntity.getId(), usuarioEntity.getStatus(),
                usuarioEntity.getNome(), usuarioEntity.getSenha(),
                new Email(usuarioEntity.getEmail()), usuarioEntity.getCargoEnum());
    }
}
