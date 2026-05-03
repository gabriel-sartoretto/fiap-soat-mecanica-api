package br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca;

import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlocacaoPecaJpaRepository extends JpaRepository<AlocacaoPecaEntity, UUID> {

    boolean existsByPrestacaoServicoIdAndPecaIdAndStatus(UUID prestacaoId, UUID pecaId, StatusRecursoEnum status);

    List<AlocacaoPecaEntity> findAllByPrestacaoServicoId(UUID prestacaoId);
}
