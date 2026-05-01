package br.com.fiap.soat.mecanica.adapters.in.web.servico.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.servico.dto.ServicoResponse;
import br.com.fiap.soat.mecanica.domain.servico.Servico;

public class ServicoResponseMapper {

    public static ServicoResponse toResponse(Servico servico) {
        return new ServicoResponse(servico.getId(), servico.getStatus(), servico.getNome(), servico.getDescricao());
    }
}
