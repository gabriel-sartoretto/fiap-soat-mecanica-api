package br.com.fiap.soat.mecanica.adapters.out.persistence.servico.mapper;

import br.com.fiap.soat.mecanica.adapters.out.persistence.servico.ServicoEntity;
import br.com.fiap.soat.mecanica.domain.servico.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {
    public ServicoEntity toEntity(Servico servico) {
        ServicoEntity servicoEntity = new ServicoEntity();
        servicoEntity.setId(servico.getId());
        servicoEntity.setStatus(servico.getStatus());
        servicoEntity.setNome(servico.getNome());
        servicoEntity.setDescricao(servico.getDescricao());
        return servicoEntity;
    }

    public Servico toDomain(ServicoEntity servicoEntity) {
        return Servico.reconstruir(
                servicoEntity.getId(),
                servicoEntity.getStatus(),
                servicoEntity.getNome(),
                servicoEntity.getDescricao()
        );
    }
}
