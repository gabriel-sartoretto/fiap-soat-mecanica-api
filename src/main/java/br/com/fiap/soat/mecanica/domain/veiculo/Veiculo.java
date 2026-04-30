package br.com.fiap.soat.mecanica.domain.veiculo;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
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

    private Integer quantidadeEixos;

    private UUID clienteId;

    public Veiculo(Placa placa, String marca, String modelo, String ano, Integer quantidadeEixos, UUID clienteId) {
        validar(placa, marca, modelo, ano, quantidadeEixos, clienteId);
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.quantidadeEixos = quantidadeEixos;
        this.clienteId = clienteId;
    }

    public static Veiculo reconstruir(
            UUID id,
            StatusRecursoEnum status,
            Placa placa,
            String marca,
            String modelo,
            String ano,
            Integer quantidadeEixos,
            UUID clienteId
    ) {
        Veiculo veiculo = new Veiculo();
        veiculo.id = id;
        veiculo.status = status;
        veiculo.placa = placa;
        veiculo.marca = marca;
        veiculo.modelo = modelo;
        veiculo.ano = ano;
        veiculo.quantidadeEixos = quantidadeEixos;
        veiculo.clienteId = clienteId;

        return veiculo;
    }

    public void alterar(String marca, String modelo, String ano, Integer quantidadeEixos) {
        validar(placa, marca, modelo, ano, quantidadeEixos, clienteId);
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.quantidadeEixos = quantidadeEixos;
    }

    private void validar(Placa placa, String marca, String modelo, String ano, Integer quantidadeEixos, UUID clienteId) {
        if (placa == null) throw new RegraNegocioException("A placa obrigatório");
        if (marca == null) throw new RegraNegocioException("A marca é obrigatório");
        if (modelo == null) throw new RegraNegocioException("O modelo inválida");
        if (ano == null) throw new RegraNegocioException("O Ano é obrigatório");
        if (quantidadeEixos == null) throw new RegraNegocioException("A quantidade de eixos é obrigatória");
        if (clienteId == null) throw new RegraNegocioException("O cliente dono é obrigatória");
    }
}
