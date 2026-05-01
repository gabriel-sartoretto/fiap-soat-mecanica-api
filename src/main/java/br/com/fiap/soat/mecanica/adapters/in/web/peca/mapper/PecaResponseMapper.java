package br.com.fiap.soat.mecanica.adapters.in.web.peca.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaResponse;
import br.com.fiap.soat.mecanica.domain.peca.Peca;

public class PecaResponseMapper {

    public static PecaResponse toResponse(Peca p) {
        return new PecaResponse(p.getId(), p.getStatus(), p.getNome(), p.getMarca(),
                p.getValorUnitario(), p.getQuantidadeEstoque());
    }
}
