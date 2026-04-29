package br.com.fiap.soat.mecanica.adapters.out.persistence.peca;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "pecas", schema = "public")
public class PecaEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nome", unique = true, nullable = false)
    private String nome;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "valor_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;
}
