package br.com.fiap.soat.mecanica.domain.usuario;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Usuario extends Principal {

    private UUID id;
    private String nome;
    private Email email;
    private String senha;
    private CargoEnum cargoEnum;

    public Usuario(String nome, String senhaHash, Email email, CargoEnum cargoEnum) {
        validar(nome, email, senhaHash, cargoEnum);
        this.nome = nome;
        this.email = email;
        this.senha = senhaHash;
        this.cargoEnum = cargoEnum;
    }

    public static Usuario reconstruir(
            UUID id,
            StatusRecursoEnum status,
            String nome,
            String senhaHash,
            Email email,
            CargoEnum cargoEnum
    ) {
        Usuario usuario = new Usuario();

        usuario.id = id;
        usuario.status = status;
        usuario.nome = nome;
        usuario.senha = senhaHash;
        usuario.email = email;
        usuario.cargoEnum = cargoEnum;

        return usuario;
    }

    private void validar(String nome, Email email, String senhaHash, CargoEnum cargoEnum) {
        if (nome == null) throw new RegraNegocioException("Nome obrigatório");
        if (email == null) throw new RegraNegocioException("E-mail é obrigatório");
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new RegraNegocioException("Senha inválida");
        }
        if (cargoEnum == null) {
            throw new RegraNegocioException("O Cargo é obrigatório");
        }
    }

    public void setId(UUID id) {
        this.id = id;
    }
}