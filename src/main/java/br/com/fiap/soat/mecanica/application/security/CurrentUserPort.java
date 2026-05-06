package br.com.fiap.soat.mecanica.application.security;

import br.com.fiap.soat.mecanica.domain.usuario.Usuario;

public interface CurrentUserPort {

    Usuario get();
}