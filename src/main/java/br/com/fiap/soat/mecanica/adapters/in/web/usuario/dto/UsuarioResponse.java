package br.com.fiap.soat.mecanica.adapters.in.web.usuario.dto;

import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;

public record UsuarioResponse (
        String nome,
        String email,
        CargoEnum cargoEnum
) {}
