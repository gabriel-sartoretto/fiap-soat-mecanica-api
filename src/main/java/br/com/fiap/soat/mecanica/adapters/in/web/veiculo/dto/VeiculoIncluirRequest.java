package br.com.fiap.soat.mecanica.adapters.in.web.veiculo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoIncluirRequest(

        @NotBlank(message = "A placa não pode ser nula ou vazia")
        String placa,

        @NotBlank(message = "A marca não pode ser nula ou vazia")
        String marca,

        @NotBlank(message = "O modelo não pode ser nulo ou vazio")
        String modelo,

        @NotBlank(message = "O ano não pode ser nulo ou vazio")
        String ano,

        @NotNull(message = "A quantiade de eixos não pode ser nula")
        int quantidadeEixos
) {}
