package br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto;

public record ClienteIncluirRequest(
        String nome,
        String cpf,
        String cnpj,
        String email,
        String telefone,
        String usuarioId
) {}
