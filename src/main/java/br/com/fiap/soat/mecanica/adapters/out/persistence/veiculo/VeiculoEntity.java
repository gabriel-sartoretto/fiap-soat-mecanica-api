package br.com.fiap.soat.mecanica.adapters.out.persistence.veiculo;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(
        name = "veiculos",
        indexes = {
                @Index(name = "idx_veiculo_cliente_id", columnList = "cliente_id"),
        })
public class VeiculoEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "placa", unique = true, nullable = false, length = 7)
    private String placa;

    @Column(name = "marca", nullable = false, length = 255)
    private String marca;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "ano", nullable = false, length = 4)
    private String ano;

    @Column(name = "quantidade_eixos", nullable = false)
    private Integer quantidadeEixos;

    @Column(name = "cliente_id", nullable = false)
    private UUID clienteId;
}
