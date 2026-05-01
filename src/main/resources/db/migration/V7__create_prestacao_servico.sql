CREATE TABLE prestacao_servicos
(
    id                    UUID PRIMARY KEY,
    status                VARCHAR(20)    NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    quantidade_necessaria INTEGER        NOT NULL,
    preco_mdo             NUMERIC(10, 2) NOT NULL,
    subtotal              NUMERIC(10, 2) NOT NULL,
    data_inicio           TIMESTAMP      NOT NULL,
    data_fim              TIMESTAMP,
    ordem_servico_id      UUID           NOT NULL,
    servico_id            UUID           NOT NULL,

    CONSTRAINT fk_prestacao_servico_ordem_servico
        FOREIGN KEY (ordem_servico_id)
            REFERENCES ordem_servicos (id),

    CONSTRAINT fk_prestacao_servico_servico
        FOREIGN KEY (servico_id)
            REFERENCES servicos (id)
);

CREATE INDEX idx_prestacao_servico_ordem_servico ON prestacao_servicos (ordem_servico_id);
CREATE INDEX idx_prestacao_servico_servico ON prestacao_servicos (servico_id);