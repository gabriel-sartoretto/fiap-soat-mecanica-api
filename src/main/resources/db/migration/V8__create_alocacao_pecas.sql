/*CREATE TABLE alocacao_pecas
(
    id                    UUID PRIMARY KEY,
    prestacao_servico_id  UUID NOT NULL,
    peca_id               UUID NOT NULL,
    quantidade_necessaria INT  NOT NULL,

    CONSTRAINT fk_prestacao_servico
        FOREIGN KEY (prestacao_servico_id) REFERENCES prestacao_servicos (id),

    CONSTRAINT fk_peca
        FOREIGN KEY (peca_id) REFERENCES pecas (id)
);*/