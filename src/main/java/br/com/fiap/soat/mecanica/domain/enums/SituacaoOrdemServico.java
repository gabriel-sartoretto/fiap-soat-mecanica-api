package br.com.fiap.soat.mecanica.domain.enums;

public enum SituacaoOrdemServico {
    RECEBIDA("Recebida"),
    EM_DIAGNOSTICO("Em diagnóstico"),
    AGUARDANDO_APROVACAO("Aguardando aprovação"),
    EM_EXECUCAO("Em execução"),
    FINALIZADA("Finalizado"),
    ENTREGUE("Entregue");

    private final String descricao;

    SituacaoOrdemServico(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
