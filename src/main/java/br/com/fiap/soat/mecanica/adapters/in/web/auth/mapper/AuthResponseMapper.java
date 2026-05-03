package br.com.fiap.soat.mecanica.adapters.in.web.auth.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.auth.dto.LoginResponse;

public class AuthResponseMapper {

    public static LoginResponse toResponse(String token) {
        return new LoginResponse(token);
    }
}
