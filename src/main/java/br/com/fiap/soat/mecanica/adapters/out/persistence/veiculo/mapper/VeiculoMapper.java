package br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo.mapper;

import br.com.fiap.soat.mecanica.domain.veiculo.Veiculo;
import br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo.VeiculoEntity;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public VeiculoEntity toEntity(Veiculo veiculo) {
        VeiculoEntity veiculoEntity = new VeiculoEntity();
        veiculoEntity.setPlaca(veiculo.getPlaca());
        veiculoEntity.setMarca(veiculo.getMarca());
        veiculoEntity.setModelo(veiculo.getModelo());
        veiculoEntity.setAno(veiculo.getAno());
        veiculoEntity.setQuantidadeEixos(veiculo.getQuantidadeEixos());
        return veiculoEntity;
    }

    public Veiculo toDomain(VeiculoEntity veiculoEntity) {
        Veiculo veiculo = new Veiculo(veiculoEntity.getPlaca(), veiculoEntity.getMarca(),
                veiculoEntity.getModelo(), veiculoEntity.getAno(), veiculoEntity.getQuantidadeEixos());
        veiculo.setId(veiculoEntity.getId());
        return veiculo;
    }
}
