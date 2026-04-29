package br.com.fiap.soat.mecanica.domain.veiculo;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.valueobject.Placa;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class Veiculo extends Principal {

    private UUID id;

    private Placa placa;

    private String marca;

    private String modelo;

    private String ano;

    private int quantidadeEixos;

    private UUID clienteId;

    public Veiculo(Placa placa, String marca, String modelo, String ano, int quantidadeEixos, UUID clienteId) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.quantidadeEixos = quantidadeEixos;
        this.clienteId = clienteId;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
