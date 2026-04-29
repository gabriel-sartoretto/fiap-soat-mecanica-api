package br.com.fiap.soat.mecanica.domain.enums;

public enum CargoEnum {
    ATENDENTE("Atendente"),
    MECANICO("Mecânico"),
    ALMOXARIFE("Almoxarife");

    private final String descricao;

    CargoEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
