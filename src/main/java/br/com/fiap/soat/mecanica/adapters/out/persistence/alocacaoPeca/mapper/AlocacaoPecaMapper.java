package br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca.AlocacaoPecaEntity;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;
import org.springframework.stereotype.Component;

@Component
public class AlocacaoPecaMapper {

    public AlocacaoPecaEntity toEntity(AlocacaoPeca alocacaoPeca) {
        AlocacaoPecaEntity alocacaoPecaEntity = new AlocacaoPecaEntity();
        alocacaoPecaEntity.setQuantidadeNecessaria(alocacaoPeca.getQuantidadeNecessaria());
        alocacaoPecaEntity.setPrestacaoServicoId(alocacaoPeca.getPrestacaoServicoId());
        alocacaoPecaEntity.setPecaId(alocacaoPeca.getPecaId());
        return alocacaoPecaEntity;
    }

    public AlocacaoPeca toDomain(AlocacaoPecaEntity alocacaoPecaEntity) {
        return AlocacaoPeca.reconstruir(
                alocacaoPecaEntity.getId(),
                alocacaoPecaEntity.getStatus(),
                alocacaoPecaEntity.getQuantidadeNecessaria(),
                alocacaoPecaEntity.getPrestacaoServicoId(),
                alocacaoPecaEntity.getPecaId()
        );
    }
}