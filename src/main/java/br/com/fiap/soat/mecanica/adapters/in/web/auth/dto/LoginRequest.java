package br.com.fiap.soat.mecanica.adapters.in.web.auth.dto;

public record LoginRequest(
    String email,
    String senha
) {}
