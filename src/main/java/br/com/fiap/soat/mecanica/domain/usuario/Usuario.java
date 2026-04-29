package br.com.fiap.soat.mecanica.domain.usuario;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import br.com.fiap.soat.mecanica.domain.valueobject.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class Usuario extends Principal {

    private UUID id;
    private String nome;
    private Email email;
    private String senha;
    private CargoEnum cargoEnum;

    public Usuario(String nome, String senhaHash, Email email, CargoEnum cargoEnum) {
        if (senhaHash == null || senhaHash.isBlank()) {
            throw new IllegalArgumentException("Senha inválida");
        }
        this.nome = nome;
        this.email = email;
        this.senha = senhaHash;
        this.cargoEnum = cargoEnum;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}