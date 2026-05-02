package br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlocacaoPecaJpaRepository extends JpaRepository<AlocacaoPecaEntity, UUID> {

    boolean existsByPrestacaoServicoIdAndPecaId(UUID prestacaoId, UUID pecaId);
}
