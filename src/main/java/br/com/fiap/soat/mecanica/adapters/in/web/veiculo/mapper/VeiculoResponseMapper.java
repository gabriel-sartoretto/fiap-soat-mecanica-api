package br.com.fiap.soat.mecanica.adapters.in.web.veiculo.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.veiculo.dto.VeiculoResponse;
import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;

public class VeiculoResponseMapper {

    public static VeiculoResponse toResponse(Veiculo v) {
        return new VeiculoResponse(v.getId(), v.getPlaca(), v.getMarca(),
                v.getModelo(), v.getAno(), v.getQuantidadeEixos());
    }
}