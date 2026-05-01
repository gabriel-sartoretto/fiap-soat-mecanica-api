package br.com.fiap.soat.mecanica.adapters.out.persistence.ordemServico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrdemServicoJpaRepository extends JpaRepository<OrdemServicoEntity, UUID> {

    List<OrdemServicoEntity> findAllByVeiculoId(UUID veiculoId);
}
