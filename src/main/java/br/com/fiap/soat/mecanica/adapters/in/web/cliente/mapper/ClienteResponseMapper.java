package br.com.fiap.soat.mecanica.adapters.in.web.cliente.mapper;

import br.com.fiap.soat.mecanica.adapters.in.web.cliente.dto.ClienteResponse;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;

public class ClienteResponseMapper {

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getStatus(), cliente.getNome(),
                cliente.getCpf() == null ? null : cliente.getCpf().getValue(),
                cliente.getCnpj() == null ? null : cliente.getCnpj().getValue(), cliente.getEmail().getValue(),
                cliente.getTelefone() == null ? null : cliente.getTelefone().getValue(), cliente.getUsuarioId());
    }
}
