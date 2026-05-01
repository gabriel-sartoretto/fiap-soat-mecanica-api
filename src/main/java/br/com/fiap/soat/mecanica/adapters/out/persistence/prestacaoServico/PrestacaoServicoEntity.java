package br.com.fiap.soat.mecanica.adapters.out.persistence.prestacaoServico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "prestacoes_servico",
        schema = "public",
        indexes = {
                @Index(name = "idx_prestacao_servico_ordem_servico_id", columnList = "ordem_servico_id"),
                @Index(name = "idx_prestacao_servico_servico_id", columnList = "servico_id")
        })
@Getter
@Setter
@NoArgsConstructor
public class PrestacaoServicoEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "quantidade_necessaria", nullable = false)
    private Integer quantidadeNecessaria;

    @Column(name = "preco_mdo", nullable = false)
    private BigDecimal precoMaoDeObra;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @Column(name = "ordem_servico_id", nullable = false)
    private UUID ordemServicoId;

    @Column(name = "servico_id", nullable = false)
    private UUID servicoId;
}