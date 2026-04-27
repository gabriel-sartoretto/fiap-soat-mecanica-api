package br.com.fiap.soat.mecanica.domain.veiculo;

import br.com.fiap.soat.mecanica.domain.Principal;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class Veiculo extends Principal {

    private UUID id;

    private String placa;

    private String marca;

    private String modelo;

    private String ano;

    private int quantidadeEixos;

    public Veiculo(String placa, String marca, String modelo, String ano, int quantidadeEixos) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.quantidadeEixos = quantidadeEixos;
    }
}
