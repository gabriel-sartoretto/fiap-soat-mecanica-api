package br.com.fiap.soat.mecanica.adapters.out.persistence.peca.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.peca.PecaEntity;
import br.com.fiap.soat.mecanica.domain.peca.Peca;
import org.springframework.stereotype.Component;

@Component
public class PecaMapper {

    public PecaEntity toEntity(Peca peca) {
        PecaEntity pecaEntity = new PecaEntity();
        pecaEntity.setId(peca.getId());
        pecaEntity.setStatus(peca.getStatus());
        pecaEntity.setNome(peca.getNome());
        pecaEntity.setMarca(peca.getMarca());
        pecaEntity.setValorUnitario(peca.getValorUnitario());
        pecaEntity.setQuantidadeEstoque(peca.getQuantidadeEstoque());
        return pecaEntity;
    }

    public Peca toDomain(PecaEntity pecaEntity) {
        Peca peca = new Peca(pecaEntity.getNome(), pecaEntity.getMarca(),
                pecaEntity.getValorUnitario(), pecaEntity.getQuantidadeEstoque());
        peca.setId(pecaEntity.getId());
        peca.setStatus(pecaEntity.getStatus());
        return peca;
    }
}
