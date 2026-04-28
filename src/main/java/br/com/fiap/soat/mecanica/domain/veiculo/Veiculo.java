package br.com.fiap.soat.mecanica.domain.veiculo;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.valueobject.Placa;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Veiculo extends Principal {

    private UUID id;

    private Placa placa;

    private String marca;

    private String modelo;

    private String ano;

    private int quantidadeEixos;

    public Veiculo(Placa placa, String marca, String modelo, String ano, int quantidadeEixos) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.quantidadeEixos = quantidadeEixos;
    }
}
