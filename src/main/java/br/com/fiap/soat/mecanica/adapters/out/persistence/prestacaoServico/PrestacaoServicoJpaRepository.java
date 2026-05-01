package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PrestacaoServicoJpaRepository extends JpaRepository<PrestacaoServicoEntity, UUID> {

    List<PrestacaoServicoEntity> findAllByOrdemServicoId(UUID ordemSevicoId);
}
