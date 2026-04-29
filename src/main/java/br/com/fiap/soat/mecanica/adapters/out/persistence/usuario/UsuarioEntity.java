package br.com.fiap.soat.mecanica.adapters.out.persistence.usuario;

import br.com.fiap.soat.mecanica.adapters.out.persistence.PrincipalEntity;
import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "usuarios", schema = "public")
public class UsuarioEntity extends PrincipalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "cargo_enum", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private CargoEnum cargoEnum;
}
