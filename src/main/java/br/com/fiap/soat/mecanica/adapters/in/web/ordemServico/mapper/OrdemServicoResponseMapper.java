package br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.ordemServico.dto.OrdemServicoResponse;
import br.com.fiap.soat.mecanica.domain.ordemServico.OrdemServico;

public class OrdemServicoResponseMapper {

    public static OrdemServicoResponse toResponse(OrdemServico os) {
        return new OrdemServicoResponse(os.getId(), os.getStatus(), os.getSituacao(), os.getDataRecebida(),
                os.getDataDiagnostico(), os.getDataExecucao(), os.getDataFinalizada(), os.getDataEntregue(),
                os.getPago(), os.getValorTotal(), os.getObservacao(), os.getVeiculoId(), os.getUsuarioId());
    }
}
