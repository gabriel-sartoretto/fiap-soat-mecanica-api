package br.com.fiap.soat.mecanica.infrastructure.persistence.veiculo.mapper;

import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.infrastructure.persistence.veiculo.VeiculoEntity;

public class VeiculoMapper {

    public static VeiculoEntity toEntity(Veiculo veiculo) {
        VeiculoEntity veiculoEntity = new VeiculoEntity();
        veiculoEntity.setPlaca(veiculo.getPlaca());
        return veiculoEntity;
    }
}
