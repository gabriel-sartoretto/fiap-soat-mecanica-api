package br.com.fiap.soat.mecanica.domain.enums;

public enum CargoEnum {
    ATENDENTE("Atendente"),
    MECANICO("Meânico"),
    ALMOXARIFADO("Almocharifado");

    private final String descricao;

    CargoEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
