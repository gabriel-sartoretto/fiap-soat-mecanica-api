CREATE TABLE ordem_servicos
(
    id                        UUID PRIMARY KEY,
    status                    VARCHAR(20)    NOT NULL CHECK (status IN ('ATIVO', 'INATIVO')),
    situacao                  VARCHAR(50)    NOT NULL,
    data_recebida             TIMESTAMP      NOT NULL,
    data_diagnostico          TIMESTAMP,
    data_aguardando_aprovacao TIMESTAMP,
    data_execucao             TIMESTAMP,
    data_finalizada           TIMESTAMP,
    data_entregue             TIMESTAMP,
    pago                      BOOLEAN,
    valor_total               NUMERIC(10, 2) NOT NULL,
    observacao                TEXT,
    veiculo_id                UUID           NOT NULL,
    usuario_id                UUID           NOT NULL,

    CONSTRAINT fk_ordem_servico_veiculo
        FOREIGN KEY (veiculo_id)
            REFERENCES veiculos (id),
    CONSTRAINT fk_ordem_servico_usuario
        FOREIGN KEY (usuario_id)
            REFERENCES usuarios (id)
);

CREATE INDEX idx_ordem_servicos_veiculo_id ON ordem_servicos (veiculo_id);
CREATE INDEX idx_ordem_servicos_usuario_id ON ordem_servicos (usuario_id);