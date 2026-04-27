package br.com.fiap.soat.mecanica.adapters.out.persistence.usuario.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.usuario.UsuarioEntity;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioEntity toEntity(Usuario Usuario) {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setNome(Usuario.getNome());
        usuarioEntity.setEmail(Usuario.getEmail());
        usuarioEntity.setSenha(Usuario.getSenha());
        usuarioEntity.setCargoEnum(Usuario.getCargoEnum());
        return usuarioEntity;
    }

    public Usuario toDomain(UsuarioEntity usuarioEntity) {
        Usuario usuario = new Usuario(
                usuarioEntity.getNome(), usuarioEntity.getEmail(),
                usuarioEntity.getSenha(), usuarioEntity.getCargoEnum());
        usuario.setId(usuarioEntity.getId());
        return usuario;
    }
}
