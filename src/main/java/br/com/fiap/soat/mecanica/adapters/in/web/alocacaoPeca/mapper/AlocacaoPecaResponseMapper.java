package br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.alocacaoPeca.dto.AlocacaoPecaResponse;
import br.com.fiap.soat.mecanica.domain.alocacaoPecas.AlocacaoPeca;

public class AlocacaoPecaResponseMapper {

    public static AlocacaoPecaResponse toResponse(AlocacaoPeca alocacaoPeca) {
        return new AlocacaoPecaResponse(alocacaoPeca.getId(), alocacaoPeca.getStatus(),
                alocacaoPeca.getQuantidadeNecessaria(), alocacaoPeca.getPrestacaoServicoId(), alocacaoPeca.getPecaId());
    }
}