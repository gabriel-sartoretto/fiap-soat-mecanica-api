package br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.prestacaoServico.dto.PrestacaoServicoResponse;
import br.com.fiap.soat.mecanica.domain.prestacaoServico.PrestacaoServico;
import org.springframework.stereotype.Component;

@Component
public class PrestacaoServicoResponseMapper {

    public static PrestacaoServicoResponse toResponse(PrestacaoServico ps) {
        return new PrestacaoServicoResponse(
                ps.getId(),
                ps.getStatus(),
                ps.getQuantidadeNecessaria(),
                ps.getPrecoMaoDeObra(),
                ps.getSubtotal(),
                ps.getDataInicio(),
                ps.getDataFim(),
                ps.getOrdemServicoId(),
                ps.getServicoId()
        );
    }
}
