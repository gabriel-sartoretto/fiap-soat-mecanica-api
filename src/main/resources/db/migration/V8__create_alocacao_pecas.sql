CREATE TABLE alocacao_pecas
(
    id                    UUID PRIMARY KEY,
    status                VARCHAR(20) NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    quantidade_necessaria INTEGER     NOT NULL,
    prestacao_servico_id  UUID        NOT NULL,
    peca_id               UUID        NOT NULL,

    CONSTRAINT fk_alocacao_prestacao
        FOREIGN KEY (prestacao_servico_id)
            REFERENCES prestacao_servicos (id),

    CONSTRAINT fk_alocacao_peca
        FOREIGN KEY (peca_id)
            REFERENCES pecas (id),

    CONSTRAINT uk_alocacao_prestacao_peca
        UNIQUE (prestacao_servico_id, peca_id)
);

CREATE INDEX idx_alocacao_prestacao
    ON alocacao_pecas (prestacao_servico_id);

CREATE INDEX idx_alocacao_peca
    ON alocacao_pecas (peca_id);