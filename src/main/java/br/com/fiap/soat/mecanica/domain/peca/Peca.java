package br.com.fiap.soat.mecanica.domain.peca;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
public class Peca extends Principal {

    private UUID id;

    private String nome;

    private String marca;

    private BigDecimal valorUnitario;

    private Integer quantidadeEstoque;

    public Peca(String nome, String marca, BigDecimal valorUnitario, Integer quantidadeEstoque) {
        validar(nome, marca, valorUnitario, quantidadeEstoque);
        this.nome = nome;
        this.marca = marca;
        this.valorUnitario = valorUnitario;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void atualizar(String nome, String marca, BigDecimal valorUnitario, Integer quantidadeEstoque) {
        validar(nome, marca, valorUnitario, quantidadeEstoque);
        this.nome = nome;
        this.marca = marca;
        this.valorUnitario = valorUnitario;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    private void validar(String nome, String marca, BigDecimal valorUnitario, Integer quantidadeEstoque) {
        if (nome == null || nome.isBlank()) {
            throw new RegraNegocioException("Nome obrigatorio");
        }
        if (marca == null || marca.isBlank()) {
            throw new RegraNegocioException("Marca obrigatoria");
        }
        if (valorUnitario == null || valorUnitario.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("Valor unitario deve ser maior ou igual a zero");
        }
        if (quantidadeEstoque == null || quantidadeEstoque < 0) {
            throw new RegraNegocioException("Quantidade em estoque deve ser maior ou igual a zero");
        }
    }
}
