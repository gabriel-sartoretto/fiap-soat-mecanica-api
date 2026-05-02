package br.com.fiap.soat.mecanica.adapters.out.persistence.ordemServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import br.com.fiap.soat.mecanica.domain.enums.SituacaoOrdemServico;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "ordem_servicos",
        schema = "public",
        indexes = {
                @Index(name = "idx_ordem_servico_veiculo_id", columnList = "veiculo_id"),
                @Index(name = "idx_ordem_servico_usuario_id", columnList = "usuario_id")
        })
@Getter
@Setter
@NoArgsConstructor
public class OrdemServicoEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacao", nullable = false)
    private SituacaoOrdemServico situacao;

    @Column(name = "data_recebida", nullable = false)
    private LocalDateTime dataRecebida;

    @Column(name = "data_diagnostico")
    private LocalDateTime dataDiagnostico;

    @Column(name = "data_execucao")
    private LocalDateTime dataExecucao;

    @Column(name = "data_finalizada")
    private LocalDateTime dataFinalizada;

    @Column(name = "data_entregue")
    private LocalDateTime dataEntregue;

    @Column(name = "pago")
    private Boolean pago;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "veiculo_id", nullable = false)
    private UUID veiculoId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;
}
