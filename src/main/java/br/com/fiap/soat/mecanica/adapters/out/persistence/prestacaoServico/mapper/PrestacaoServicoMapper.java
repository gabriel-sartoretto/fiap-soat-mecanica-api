package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.PrestacaoServicoEntity;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import org.springframework.stereotype.Component;

@Component
public class PrestacaoServicoMapper {

    public PrestacaoServicoEntity toEntity(PrestacaoServico prestacaoServico) {
        PrestacaoServicoEntity prestacaoServicoEntity = new PrestacaoServicoEntity();
        prestacaoServicoEntity.setQuantidadeNecessaria(prestacaoServico.getQuantidadeNecessaria());
        prestacaoServicoEntity.setPrecoMaoDeObra(prestacaoServico.getPrecoMaoDeObra());
        prestacaoServicoEntity.setSubtotal(prestacaoServico.getSubtotal());
        prestacaoServicoEntity.setDataInicio(prestacaoServico.getDataInicio());
        prestacaoServicoEntity.setDataFim(prestacaoServico.getDataFim());
        prestacaoServicoEntity.setOrdemServicoId(prestacaoServico.getOrdemServicoId());
        prestacaoServicoEntity.setServicoId(prestacaoServico.getServicoId());
        return prestacaoServicoEntity;
    }

    public PrestacaoServico toDomain(PrestacaoServicoEntity prestacaoServicoEntity) {
        return PrestacaoServico.reconstruir(
                prestacaoServicoEntity.getId(),
                prestacaoServicoEntity.getStatus(),
                prestacaoServicoEntity.getQuantidadeNecessaria(),
                prestacaoServicoEntity.getPrecoMaoDeObra(),
                prestacaoServicoEntity.getSubtotal(),
                prestacaoServicoEntity.getDataInicio(),
                prestacaoServicoEntity.getDataFim(),
                prestacaoServicoEntity.getOrdemServicoId(),
                prestacaoServicoEntity.getServicoId()
        );
    }
}
