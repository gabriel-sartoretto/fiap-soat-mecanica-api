package br.com.fiap.soat.mecanica.config.exception;

import java.util.List;

public record ErrorResponse(
        int status,
        String erro,
        String mensagem,
        String campo,
        Object valorRecebido,
        List<String> detalhes
) {}
