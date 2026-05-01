package br.com.fiap.soat.mecanica.adapters.out.persistence.servico;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "servicos", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class ServicoEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "descricao")
    private String descricao;
}
