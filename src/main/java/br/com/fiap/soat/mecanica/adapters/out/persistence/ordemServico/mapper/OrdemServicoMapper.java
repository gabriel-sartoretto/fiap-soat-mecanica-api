package br.com.fiap.soat.mecanica.adapters.out.persistence.ordemServico.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.ordemServico.OrdemServicoEntity;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;
import org.springframework.stereotype.Component;

@Component
public class OrdemServicoMapper {

    public OrdemServicoEntity toEntity(OrdemServico ordemServico) {
        OrdemServicoEntity ordemServicoEntity = new OrdemServicoEntity();
        ordemServicoEntity.setId(ordemServico.getId());
        ordemServicoEntity.setStatus(ordemServico.getStatus());
        ordemServicoEntity.setSituacao(ordemServico.getSituacao());
        ordemServicoEntity.setDataRecebida(ordemServico.getDataRecebida());
        ordemServicoEntity.setDataDiagnostico(ordemServico.getDataDiagnostico());
        ordemServicoEntity.setDataExecucao(ordemServico.getDataExecucao());
        ordemServicoEntity.setDataFinalizada(ordemServico.getDataFinalizada());
        ordemServicoEntity.setDataEntregue(ordemServico.getDataEntregue());
        ordemServicoEntity.setPago(ordemServico.getPago());
        ordemServicoEntity.setValorTotal(ordemServico.getValorTotal());
        ordemServicoEntity.setObservacao(ordemServico.getObservacao());
        ordemServicoEntity.setVeiculoId(ordemServico.getVeiculoId());
        ordemServicoEntity.setUsuarioId(ordemServico.getUsuarioId());
        return ordemServicoEntity;
    }

    public OrdemServico toDomain(OrdemServicoEntity ordemServicoEntity) {
        return OrdemServico.reconstruir(
                ordemServicoEntity.getId(),
                ordemServicoEntity.getStatus(),
                ordemServicoEntity.getSituacao(),
                ordemServicoEntity.getDataRecebida(),
                ordemServicoEntity.getDataDiagnostico(),
                ordemServicoEntity.getDataExecucao(),
                ordemServicoEntity.getDataFinalizada(),
                ordemServicoEntity.getDataEntregue(),
                ordemServicoEntity.getPago(),
                ordemServicoEntity.getValorTotal(),
                ordemServicoEntity.getObservacao(),
                ordemServicoEntity.getVeiculoId(),
                ordemServicoEntity.getUsuarioId()
        );
    }
}
