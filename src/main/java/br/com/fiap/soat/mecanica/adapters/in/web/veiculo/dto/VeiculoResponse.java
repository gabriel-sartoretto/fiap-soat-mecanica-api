package br.com.fiap.soat.mecanica.adapters.in.web.veiculo.dto;

import java.util.UUID;

public record VeiculoResponse(

        UUID id,
        String placa,
        String marca,
        String modelo,
        String ano,
        int quantidadeEixos
) {}
