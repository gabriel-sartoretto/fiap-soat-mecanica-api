package br.com.fiap.soat.mecanica.domain.peca;

import br.com.fiap.soat.mecanica.domain.Principal;
import br.com.fiap.soat.mecanica.domain.enums.StatusRecursoEnum;
import br.com.fiap.soat.mecanica.domain.exception.RegraNegocioException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static Peca reconstruir(
            UUID id,
            StatusRecursoEnum status,
            String nome,
            String marca,
            BigDecimal valorUnitario,
            Integer quantidadeEstoque
    ) {
        Peca peca = new Peca();
        peca.id = id;
        peca.status = status;
        peca.nome = nome;
        peca.marca = marca;
        peca.valorUnitario = valorUnitario;
        peca.quantidadeEstoque = quantidadeEstoque;
        return peca;
    }

    public void alterar(String nome, String marca, BigDecimal valorUnitario, Integer quantidadeEstoque) {
        validar(nome, marca, valorUnitario, quantidadeEstoque);
        this.nome = nome;
        this.marca = marca;
        this.valorUnitario = valorUnitario;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void setId(UUID id) {
        this.id = id;
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
