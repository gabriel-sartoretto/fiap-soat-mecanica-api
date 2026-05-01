package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico.projection.TempoMedioServicoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PrestacaoServicoJpaRepository extends JpaRepository<PrestacaoServicoEntity, UUID> {

    List<PrestacaoServicoEntity> findAllByOrdemServicoId(UUID ordemSevicoId);

    boolean existsByOrdemServicoIdAndServicoId(UUID ordemServicoId, UUID servicoId);

    @Query(value = """
                SELECT 
                    s.nome as nomeServico,
                    AVG(EXTRACT(EPOCH FROM (p.data_fim - p.data_inicio))) as tempoMedioSegundos
                FROM prestacao_servicos p
                JOIN servicos s ON s.id = p.servico_id
                WHERE p.servico_id IN (:servicoIds)
                  AND p.data_fim IS NOT NULL
                  AND p.data_inicio IS NOT NULL
                  AND p.data_fim > p.data_inicio
                GROUP BY p.servico_id, s.nome
            """, nativeQuery = true)
    List<TempoMedioServicoProjection> calcularTempoMedioPorServicos(Set<UUID> servicoIds);
}
