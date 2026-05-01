package br.com.fiap.soat.mecanica.adapters.out.persistence.peca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "pecas", schema = "public")
public class PecaEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nome", length = 255, unique = true, nullable = false)
    private String nome;

    @Column(name = "marca", length = 255, nullable = false)
    private String marca;

    @Column(name = "valor_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;
}
