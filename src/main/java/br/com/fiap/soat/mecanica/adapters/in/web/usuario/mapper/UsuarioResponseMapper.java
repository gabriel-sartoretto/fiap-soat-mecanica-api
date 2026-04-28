package br.com.fiap.soat.mecanica.adapters.in.web.usuario.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.usuario.dto.UsuarioResponse;
import br.com.fiap.soat.mecanica.domain.usuario.Usuario;

public class UsuarioResponseMapper {

    public static UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getStatus(), u.getNome(), u.getEmail().getValue(), u.getCargoEnum());
    }
}
