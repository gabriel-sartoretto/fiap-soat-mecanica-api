package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.PrestacaoServicoEntity;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import org.springframework.stereotype.Component;

@Component
public class PrestacaoServicoMapper {

    public PrestacaoServicoEntity toEntity(PrestacaoServico ps) {
        PrestacaoServicoEntity entity = new PrestacaoServicoEntity();
        entity.setId(ps.getId());
        entity.setStatus(ps.getStatus());
        entity.setQuantidadeNecessaria(ps.getQuantidadeNecessaria());
        entity.setPrecoMaoDeObra(ps.getPrecoMaoDeObra());
        entity.setSubtotal(ps.getSubtotal());
        entity.setDataInicio(ps.getDataInicio());
        entity.setDataFim(ps.getDataFim());
        entity.setOrdemServicoId(ps.getOrdemServicoId());
        entity.setServicoId(ps.getServicoId());
        return entity;
    }

    public PrestacaoServico toDomain(PrestacaoServicoEntity entity) {
        return PrestacaoServico.reconstruir(
                entity.getId(),
                entity.getStatus(),
                entity.getQuantidadeNecessaria(),
                entity.getPrecoMaoDeObra(),
                entity.getSubtotal(),
                entity.getDataInicio(),
                entity.getDataFim(),
                entity.getOrdemServicoId(),
                entity.getServicoId()
        );
    }
}
