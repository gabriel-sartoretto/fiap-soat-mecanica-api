package br.com.fiap.soat.mecanica.domain.enums;

public enum OperacaoSubtotalEnum {
    SOMAR("Somar"),
    SUBTRAIR("Subtrair");

    private final String descricao;

    OperacaoSubtotalEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
