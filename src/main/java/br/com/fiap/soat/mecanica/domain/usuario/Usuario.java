package br.com.fiap.soat.mecanica.domain.usuario;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.CargoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Usuario extends Principal {

    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private CargoEnum cargoEnum;

    public Usuario(String nome, String email, String senha, CargoEnum cargoEnum) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cargoEnum = cargoEnum;
    }
}