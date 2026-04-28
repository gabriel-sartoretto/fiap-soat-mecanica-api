package br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;

import java.util.UUID;

public record ClienteResponse(
        UUID id,
        StatusRecursoEnum status,
        String nome,
        String cpf,
        String cnpj,
        String email,
        String telefone,
        UUID usuarioId
) {}
