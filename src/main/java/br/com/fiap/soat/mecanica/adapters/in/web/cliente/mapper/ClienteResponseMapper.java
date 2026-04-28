package br.com.fiap.soat.mecanica.adapters.in.web.cliente.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto.ClienteResponse;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;

public class ClienteResponseMapper {

    public static ClienteResponse toResponse(Cliente c) {
        return new ClienteResponse(c.getId(), c.getStatus(), c.getNome(), c.getCpf().getValue(),
                c.getCnpj() == null ? null : c.getCnpj().getValue(), c.getEmail().getValue(),
                c.getTelefone() == null ? null : c.getTelefone().getValue(), c.getUsuarioId());
    }
}
