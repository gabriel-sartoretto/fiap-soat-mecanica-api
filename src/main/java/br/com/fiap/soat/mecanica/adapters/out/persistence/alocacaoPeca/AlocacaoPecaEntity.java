package br.com.fiap.soat.mecanica.adapters.out.persistence.alocacaoPeca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "alocacao_pecas",
        schema = "public",
        indexes = {
                @Index(name = "idx_alocacao_prestacao", columnList = "prestacao_servico_id"),
                @Index(name = "idx_alocacao_peca", columnList = "peca_id")
        })
@Getter
@Setter
@NoArgsConstructor
public class AlocacaoPecaEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "quantidade_necessaria", nullable = false)
    private Integer quantidadeNecessaria;

    @Column(name = "prestacao_servico_id", nullable = false)
    private UUID prestacaoServicoId;

    @Column(name = "peca_id", nullable = false)
    private UUID pecaId;
}