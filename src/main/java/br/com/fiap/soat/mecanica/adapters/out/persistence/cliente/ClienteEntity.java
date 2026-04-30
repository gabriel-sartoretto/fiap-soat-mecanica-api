package br.com.fiap.soat.mecanica.adapters.out.persistence.cliente;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(
        name = "clientes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "cpf"),
                @UniqueConstraint(columnNames = "cnpj"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ClienteEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "cpf", length = 11)
    private String cpf;

    @Column(name = "cnpj", length = 14)
    private String cnpj;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;
}
