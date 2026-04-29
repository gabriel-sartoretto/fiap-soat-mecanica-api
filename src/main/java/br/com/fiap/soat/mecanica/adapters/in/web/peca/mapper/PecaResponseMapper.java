package br.com.fiap.soat.mecanica.adapters.in.web.peca.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaAtualizarRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaIncluirRequest;
import br.com.fiap.soat.mecanica.adapters.in.web.peca.dto.PecaResponse;
import br.com.fiap.soat.mecanica.domain.peca.Peca;

public class PecaResponseMapper {

    public static Peca toDomain(PecaIncluirRequest request) {
        return new Peca(request.nome(), request.marca(), request.valorUnitario(), request.quantidadeEstoque());
    }

    public static void atualizarDomain(Peca peca, PecaAtualizarRequest request) {
        peca.atualizar(request.nome(), request.marca(), request.valorUnitario(), request.quantidadeEstoque());
    }

    public static PecaResponse toResponse(Peca p) {
        return new PecaResponse(p.getId(), p.getStatus(), p.getNome(), p.getMarca(),
                p.getValorUnitario(), p.getQuantidadeEstoque());
    }
}
