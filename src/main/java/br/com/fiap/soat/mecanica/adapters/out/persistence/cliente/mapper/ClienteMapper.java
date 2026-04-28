package br.com.fiap.soat.mecanica.adapters.out.persistence.cliente.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.cliente.ClienteEntity;
import br.com.fiap.soat.mecanica.domain.cliente.Cliente;
import br.com.fiap.soat.mecanica.domain.valueobject.CNPJ;
import br.com.fiap.soat.mecanica.domain.valueobject.CPF;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import br.com.fiap.soat.mecanica.domain.valueobject.Telefone;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteEntity toEntity(Cliente cliente) {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setNome(cliente.getNome());
        clienteEntity.setCpf(cliente.getCpf().getValue());
        clienteEntity.setCnpj(cliente.getCnpj() == null ? null : cliente.getCnpj().getValue());
        clienteEntity.setEmail(cliente.getEmail().getValue());
        clienteEntity.setTelefone(cliente.getTelefone() == null ? null : cliente.getTelefone().getValue());
        clienteEntity.setUsuarioId(cliente.getUsuarioId());
        return clienteEntity;
    }

    public Cliente toDomain(ClienteEntity clienteEntity) {
        Cliente cliente = new Cliente(clienteEntity.getNome(), new CPF(clienteEntity.getCpf()),
                new CNPJ(clienteEntity.getCnpj()), new Email(clienteEntity.getEmail()),
                new Telefone(clienteEntity.getTelefone()), clienteEntity.getUsuarioId());
        cliente.setId(clienteEntity.getId());
        return cliente;
    }
}
